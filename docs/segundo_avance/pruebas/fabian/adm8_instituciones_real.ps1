$ErrorActionPreference = 'Stop'

if (-not $env:TEST_USER -or -not $env:TEST_PASSWORD) {
    throw 'Defina TEST_USER y TEST_PASSWORD solo en la sesion que ejecuta la prueba.'
}

$origen = 'http://127.0.0.1:5080'
$nombreInstitucion = 'Institucion Auditoria Fabian'
$nombreActualizado = 'Institucion Auditoria Fabian Actualizada'
$nombreCarrera = 'Carrera Auditoria Fabian'
$institucionId = $null
$carreraId = $null

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

    $instituciones = Solicitar 'GET' '/gateway/admin/institucion'
    $carreras = Solicitar 'GET' '/gateway/admin/carrera'
    $profesores = Solicitar 'GET' '/gateway/admin/profesor'
    if ($instituciones.StatusCode -ne 200 -or $carreras.StatusCode -ne 200 -or
        $profesores.StatusCode -ne 200) { throw 'No se pudieron comprobar los datos previos.' }
    if (@($instituciones.Content | ConvertFrom-Json | Where-Object {
        $_.nombre -in @($nombreInstitucion, $nombreActualizado)
    }).Count -gt 0) { throw 'La institucion de prueba ya existe.' }
    if (@($carreras.Content | ConvertFrom-Json | Where-Object {
        $_.nombre -eq $nombreCarrera
    }).Count -gt 0) { throw 'La carrera de prueba ya existe.' }
    $directorId = @($profesores.Content | ConvertFrom-Json | Select-Object -First 1)[0].id
    if (-not $directorId) { throw 'No existe un profesor director disponible para la prueba.' }

    $altaInstitucion = Solicitar 'POST' '/gateway/admin/institucion' @{ nombre = $nombreInstitucion }
    if ($altaInstitucion.StatusCode -ne 201) { throw "Alta de institucion HTTP $($altaInstitucion.StatusCode)" }
    $institucionId = ($altaInstitucion.Content | ConvertFrom-Json).id
    if (-not $institucionId) { throw 'El alta de institucion no devolvio su ID.' }
    Write-Output 'Alta ADM8: institucion temporal creada'

    $edicion = Solicitar 'PUT' "/gateway/admin/institucion/$institucionId" @{ nombre = $nombreActualizado }
    if ($edicion.StatusCode -ne 200) { throw "Edicion de institucion HTTP $($edicion.StatusCode)" }
    $detalle = Solicitar 'GET' "/gateway/admin/institucion/$institucionId"
    if ($detalle.StatusCode -ne 200 -or ($detalle.Content | ConvertFrom-Json).nombre -ne $nombreActualizado) {
        throw 'La edicion de la institucion no quedo persistida.'
    }
    Write-Output 'Edicion ADM8: nombre actualizado y persistido'

    $altaCarrera = Solicitar 'POST' '/gateway/admin/carrera' @{
        nombre = $nombreCarrera
        institucionId = $institucionId
        directorProfesorId = $directorId
    }
    if ($altaCarrera.StatusCode -ne 201) { throw "Alta de carrera vinculada HTTP $($altaCarrera.StatusCode)" }
    $carreraId = ($altaCarrera.Content | ConvertFrom-Json).id
    if (-not $carreraId) { throw 'El alta de carrera no devolvio su ID.' }
    Write-Output 'Referencia ADM8: carrera temporal vinculada'

    $bloqueo = Solicitar 'DELETE' "/gateway/admin/institucion/$institucionId"
    if ($bloqueo.StatusCode -eq 204) { $institucionId = $null }
    if ($bloqueo.StatusCode -ne 409) { throw "Borrado con referencia HTTP $($bloqueo.StatusCode); se esperaba 409" }
    $detalle = Solicitar 'GET' "/gateway/admin/institucion/$institucionId"
    if ($detalle.StatusCode -ne 200) { throw 'La institucion no permanecio despues del rechazo.' }
    Write-Output 'Referencia ADM8: borrado rechazado con HTTP 409; institucion conservada'

    $bajaCarrera = Solicitar 'DELETE' "/gateway/admin/carrera/$carreraId"
    if ($bajaCarrera.StatusCode -ne 204) { throw "Baja de carrera temporal HTTP $($bajaCarrera.StatusCode)" }
    $carreraId = $null
    $bajaInstitucion = Solicitar 'DELETE' "/gateway/admin/institucion/$institucionId"
    if ($bajaInstitucion.StatusCode -ne 204) { throw "Baja de institucion temporal HTTP $($bajaInstitucion.StatusCode)" }
    $idEliminado = $institucionId
    $institucionId = $null
    $final = Solicitar 'GET' "/gateway/admin/institucion/$idEliminado"
    if ($final.StatusCode -ne 404) { throw "Consulta despues de la baja HTTP $($final.StatusCode)" }
    Write-Output 'Limpieza ADM8: carrera e institucion temporales eliminadas'
} finally {
    if ($token -and $carreraId) {
        try {
            $limpiezaCarrera = Solicitar 'DELETE' "/gateway/admin/carrera/$carreraId"
            Write-Output "Limpieza de carrera: HTTP $($limpiezaCarrera.StatusCode)"
        } catch { Write-Output "Limpieza pendiente: carrera $carreraId" }
    }
    if ($token -and $institucionId) {
        try {
            $limpiezaInstitucion = Solicitar 'DELETE' "/gateway/admin/institucion/$institucionId"
            Write-Output "Limpieza de institucion: HTTP $($limpiezaInstitucion.StatusCode)"
        } catch { Write-Output "Limpieza pendiente: institucion $institucionId" }
    }
    $token = $null
}
