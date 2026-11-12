$ErrorActionPreference = 'Stop'

if (-not $env:TEST_USER -or -not $env:TEST_PASSWORD) {
    throw 'Defina TEST_USER y TEST_PASSWORD solo en la sesion que ejecuta la prueba.'
}

$origen = 'http://127.0.0.1:5080'
$correo = 'fabian.auditoria@cuc.cr'
$ruta = '/gateway/admin/usuario/' + [uri]::EscapeDataString($correo)
$creado = $false

function Solicitar([string]$metodo, [string]$ruta, [object]$datos = $null) {
    $opciones = @{
        Uri = $origen + $ruta
        Method = $metodo
        Headers = @{ Authorization = "Bearer $token" }
        SkipHttpErrorCheck = $true
        TimeoutSec = 15
    }
    if ($null -ne $datos) {
        $opciones.ContentType = 'application/json'
        $opciones.Body = $datos | ConvertTo-Json -Compress
    }
    return Invoke-WebRequest @opciones
}

try {
    $ingreso = Invoke-WebRequest -Uri "$origen/gateway/auth/login" -Method Post -Headers @{
        usuario = $env:TEST_USER
        contrasena = $env:TEST_PASSWORD
    } -SkipHttpErrorCheck -TimeoutSec 15
    if ($ingreso.StatusCode -notin @(200, 201)) { throw "Inicio de sesion HTTP $($ingreso.StatusCode)" }
    $token = ($ingreso.Content | ConvertFrom-Json).access_token
    if (-not $token) { throw 'No se recibio token de acceso.' }

    $inicial = Solicitar 'GET' $ruta
    if ($inicial.StatusCode -ne 404) { throw "La cuenta de prueba ya existe o no se puede consultar: HTTP $($inicial.StatusCode)" }

    $antes = Invoke-WebRequest -Uri 'http://127.0.0.1:5159/bitacora' -Headers @{
        Authorization = "Bearer $token"
    } -SkipHttpErrorCheck -TimeoutSec 15
    if ($antes.StatusCode -ne 200) { throw "Bitacora inicial HTTP $($antes.StatusCode)" }
    $ultimoEvento = @($antes.Content | ConvertFrom-Json | ForEach-Object { $_.idBitacora } |
        Measure-Object -Maximum)[0].Maximum

    $usuario = @{
        email = $correo
        tipoIdentificacion = 'Cedula'
        identificacion = 'FABIAN-AUDITORIA-2026'
        nombre = 'Fabian Auditoria'
        idRol = 'ESTUDIANTE'
        contrasena = ([guid]::NewGuid().ToString('N') + 'Aa1!')
    }
    $alta = Solicitar 'POST' '/gateway/admin/usuario' $usuario
    if ($alta.StatusCode -ne 201) { throw "Alta HTTP $($alta.StatusCode)" }
    $creado = $true
    Write-Output 'Alta ADM4: HTTP 201'

    $consulta = Solicitar 'GET' $ruta
    $registro = $consulta.Content | ConvertFrom-Json
    if ($consulta.StatusCode -ne 200 -or $registro.nombre -ne 'Fabian Auditoria' -or
        -not $registro.fechaModificacion) { throw "Detalle despues del alta: HTTP $($consulta.StatusCode)" }
    Write-Output 'Detalle ADM4: datos y fecha de modificacion presentes'

    $cambio = @{
        tipoIdentificacion = $usuario.tipoIdentificacion
        identificacion = $usuario.identificacion
        nombre = 'Fabian Auditoria Actualizado'
        idRol = $usuario.idRol
    }
    $edicion = Solicitar 'PUT' $ruta $cambio
    if ($edicion.StatusCode -ne 200) { throw "Edicion HTTP $($edicion.StatusCode)" }
    $consulta = Solicitar 'GET' $ruta
    $registro = $consulta.Content | ConvertFrom-Json
    if ($consulta.StatusCode -ne 200 -or $registro.nombre -ne $cambio.nombre -or
        -not $registro.fechaModificacion) { throw "Detalle despues de la edicion: HTTP $($consulta.StatusCode)" }
    Write-Output 'Edicion ADM4: cambio persistido y fecha presente'

    $baja = Solicitar 'DELETE' $ruta
    if ($baja.StatusCode -ne 204) { throw "Baja HTTP $($baja.StatusCode)" }
    $creado = $false
    $final = Solicitar 'GET' $ruta
    if ($final.StatusCode -ne 404) { throw "Detalle despues de la baja: HTTP $($final.StatusCode)" }
    Write-Output 'Baja ADM4: cuenta de prueba eliminada'

    $despues = Invoke-WebRequest -Uri 'http://127.0.0.1:5159/bitacora' -Headers @{
        Authorization = "Bearer $token"
    } -SkipHttpErrorCheck -TimeoutSec 15
    if ($despues.StatusCode -ne 200) { throw "Bitacora final HTTP $($despues.StatusCode)" }
    $eventos = @($despues.Content | ConvertFrom-Json | Where-Object {
        $_.idBitacora -gt $ultimoEvento -and $_.descripcion -like "*$correo*"
    })
    if ($eventos.Count -lt 3) { throw "Se esperaban tres eventos de bitacora; se encontraron $($eventos.Count)." }
    Write-Output "Bitacora ADM4: $($eventos.Count) eventos de la cuenta de prueba"
} finally {
    if ($creado -and $token) {
        try {
            $limpieza = Solicitar 'DELETE' $ruta
            Write-Output "Limpieza de la cuenta de prueba: HTTP $($limpieza.StatusCode)"
        } catch {
            Write-Output 'Limpieza pendiente: revise la cuenta fabian.auditoria@cuc.cr.'
        }
    }
    $token = $null
}
