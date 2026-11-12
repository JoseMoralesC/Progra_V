param(
    [Parameter(Mandatory = $true)][string]$Usuario,
    [Parameter(Mandatory = $true)][string]$Password
)

$ErrorActionPreference = 'Stop'
$base = 'http://localhost:5080'
$marca = -join ((65..90) | Get-Random -Count 6 | ForEach-Object { [char]$_ })
$resultados = [System.Collections.Generic.List[object]]::new()
$creados = @{}

function Invocar([string]$Metodo, [string]$Ruta, $Cuerpo = $null, [int[]]$Esperados = @(200)) {
    $parametros = @{
        Method = $Metodo
        Uri = if ($Ruta -match '^https?://') { $Ruta } else { $base + $Ruta }
        Headers = $script:headers
        UseBasicParsing = $true
        TimeoutSec = 30
    }
    if ($null -ne $Cuerpo) {
        $parametros.ContentType = 'application/json'
        # -InputObject conserva arreglos de un solo elemento como JSON (`["ADMIN"]`).
        $parametros.Body = ConvertTo-Json -InputObject $Cuerpo -Depth 8
    }
    try {
        $respuesta = Invoke-WebRequest @parametros
        $estado = [int]$respuesta.StatusCode
        $contenido = if ($respuesta.Content) { $respuesta.Content | ConvertFrom-Json } else { $null }
    } catch {
        $estado = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { 0 }
        $contenido = $null
    }
    $resultados.Add([pscustomobject]@{ Metodo=$Metodo; Ruta=$Ruta; Estado=$estado; Correcto=($estado -in $Esperados) })
    if ($estado -notin $Esperados) { throw "$Metodo $Ruta devolvio $estado; esperaba $($Esperados -join '/')." }
    return $contenido
}

$login = Invoke-WebRequest -Method Post -Uri "$base/gateway/auth/login" -Headers @{ usuario=$Usuario; contrasena=$Password } -UseBasicParsing -TimeoutSec 20
if ($login.StatusCode -ne 201) { throw "Login devolvio $($login.StatusCode)." }
$token = ($login.Content | ConvertFrom-Json).access_token
$script:headers = @{ Authorization = "Bearer $token" }
$resultados.Add([pscustomobject]@{ Metodo='POST'; Ruta='/gateway/auth/login'; Estado=201; Correcto=$true })

try {
    $instituciones = Invocar GET '/gateway/admin/institucion'
    $institucionId = @($instituciones)[0].id
    if (!$institucionId) { throw 'No hay una institucion disponible.' }

    $dominio = (Invocar GET '/gateway/admin/parametro/DOMPROF').valor
    if (!$dominio) { throw 'DOMPROF no tiene valor.' }

    $moduloId = "EV_$marca"
    $modulo = Invocar POST '/gateway/admin/modulo' @{ idModulo=$moduloId; nombreModulo="Evidencia $marca"; activo=$true; orden=97 } @(201)
    $creados.modulo = $moduloId
    Invocar PUT "/gateway/admin/modulo/$moduloId" @{ idModulo=$moduloId; nombreModulo="Evidencia Temporal $marca"; activo=$true; orden=98 } @(200) | Out-Null
    Invocar PUT "/gateway/admin/modulo/$moduloId/roles" @('ADMIN') @(204) | Out-Null
    Invocar GET "/gateway/admin/modulo/$moduloId/roles" @(200) | Out-Null

    $numero = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds().ToString()
    $identificacion = $numero.Substring($numero.Length - 9)
    $telefono = '7' + $numero.Substring($numero.Length - 7)
    $profesorBody = @{
        tipoIdentificacion='NACIONAL'
        identificacion=$identificacion
        email=("evidencia.$($marca.ToLower())@$dominio")
        nombreCompleto="Profesor Evidencia $marca"
        fechaNacimiento='1990-01-15'
        telefonos=@($telefono)
    }
    $profesor = Invocar POST '/gateway/admin/profesor' $profesorBody @(201)
    $profesorId = if ($profesor.id) { $profesor.id } else { $profesor.profesorId }
    if (!$profesorId) { throw 'Profesor creado sin identificador.' }
    $creados.profesor = $profesorId
    $profesorBody.nombreCompleto = "Profesor Temporal $marca"
    Invocar PUT "/gateway/admin/profesor/$profesorId" $profesorBody @(200) | Out-Null
    Invocar GET "/gateway/admin/profesor/buscar?texto=$marca&page=0&size=10&sort=nombreCompleto&direction=asc" @(200) | Out-Null

    $carrera = Invocar POST '/gateway/admin/carrera' @{ nombre="Carrera Evidencia $marca"; institucionId=$institucionId; directorProfesorId=$profesorId } @(201)
    $carreraId = if ($carrera.id) { $carrera.id } else { $carrera.carreraId }
    if (!$carreraId) { throw 'Carrera creada sin identificador.' }
    $creados.carrera = $carreraId
    Invocar PUT "/gateway/admin/carrera/$carreraId" @{ nombre="Carrera Temporal $marca"; institucionId=$institucionId; directorProfesorId=$profesorId } @(200) | Out-Null
    Invocar GET "/gateway/admin/carrera?institucionId=$institucionId" @(200) | Out-Null

    $curso = Invocar POST '/gateway/admin/curso' @{ carreraId=$carreraId; nivel=1; nombre="Curso Evidencia $marca" } @(201)
    $cursoId = if ($curso.cursoId) { $curso.cursoId } else { $curso.id }
    if (!$cursoId) { throw 'Curso creado sin identificador.' }
    $creados.curso = $cursoId
    Invocar PUT "/gateway/admin/curso/$cursoId" @{ carreraId=$carreraId; nivel=2; nombre="Curso Temporal $marca" } @(200) | Out-Null
    Invocar GET "/gateway/admin/curso?carreraId=$carreraId&nivel=2" @(200) | Out-Null

    $anio = 2099
    $numeroPeriodo = Get-Random -Minimum 100 -Maximum 250
    $periodo = Invocar POST '/gateway/admin/periodo' @{ anio=$anio; numeroPeriodo=$numeroPeriodo; fechaInicio="$anio-01-10"; fechaFin="$anio-03-20" } @(201)
    $periodoId = if ($periodo.periodoId) { $periodo.periodoId } else { $periodo.id }
    if (!$periodoId) { throw 'Periodo creado sin identificador.' }
    $creados.periodo = $periodoId
    Invocar PUT "/gateway/admin/periodo/$periodoId" @{ periodoId=$periodoId; anio=$anio; numeroPeriodo=$numeroPeriodo; fechaInicio="$anio-01-15"; fechaFin="$anio-03-25" } @(204) | Out-Null
    Invocar GET "/gateway/admin/periodo?anio=$anio&estado=FUTURO" @(200) | Out-Null

    # La cuenta puede operar los mantenimientos aunque no tenga la pantalla ADM3;
    # consulto el servicio propietario para comprobar la persistencia de auditoría.
    $bitacoras = Invocar GET 'http://localhost:5159/bitacora'
    $coincidencias = @($bitacoras | Where-Object { $_.descripcion -match $marca }).Count
    $resultados.Add([pscustomobject]@{ Metodo='CHECK'; Ruta='bitacora por marca'; Estado=$coincidencias; Correcto=($coincidencias -gt 0) })
} finally {
    if ($creados.curso) { try { Invocar DELETE "/gateway/admin/curso/$($creados.curso)" $null @(204) | Out-Null } catch {} }
    if ($creados.carrera) { try { Invocar DELETE "/gateway/admin/carrera/$($creados.carrera)" $null @(204) | Out-Null } catch {} }
    if ($creados.profesor) { try { Invocar DELETE "/gateway/admin/profesor/$($creados.profesor)" $null @(204) | Out-Null } catch {} }
    if ($creados.periodo) { try { Invocar DELETE "/gateway/admin/periodo/$($creados.periodo)" $null @(204) | Out-Null } catch {} }
    if ($creados.modulo) { try { Invocar DELETE "/gateway/admin/modulo/$($creados.modulo)" $null @(200) | Out-Null } catch {} }
}

$salida = [pscustomobject]@{
    fecha = (Get-Date).ToString('o')
    marca = $marca
    exitosos = @($resultados | Where-Object Correcto).Count
    total = $resultados.Count
    resultados = $resultados
    limpieza = $creados.Keys
}
$salida | ConvertTo-Json -Depth 8
if (@($resultados | Where-Object { !$_.Correcto }).Count) { exit 1 }
