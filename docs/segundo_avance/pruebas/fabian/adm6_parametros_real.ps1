$ErrorActionPreference = 'Stop'

if (-not $env:TEST_USER -or -not $env:TEST_PASSWORD) {
    throw 'Defina TEST_USER y TEST_PASSWORD solo en la sesion que ejecuta la prueba.'
}

$origen = 'http://127.0.0.1:5080'
$id = 'FABAUDIT'
$ruta = "/gateway/admin/parametro/$id"
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
    if ($inicial.StatusCode -ne 404) { throw "El ID de prueba ya existe o no se puede consultar: HTTP $($inicial.StatusCode)" }

    $antes = Invoke-WebRequest -Uri 'http://127.0.0.1:5159/bitacora' -Headers @{
        Authorization = "Bearer $token"
    } -SkipHttpErrorCheck -TimeoutSec 15
    if ($antes.StatusCode -ne 200) { throw "Bitacora inicial HTTP $($antes.StatusCode)" }
    $ultimoEvento = @($antes.Content | ConvertFrom-Json | ForEach-Object { $_.idBitacora } |
        Measure-Object -Maximum)[0].Maximum

    $alta = Solicitar 'POST' '/gateway/admin/parametro' @{ idParametro = $id; valor = 'valor inicial de prueba' }
    if ($alta.StatusCode -ne 201) { throw "Alta HTTP $($alta.StatusCode)" }
    $creado = $true
    Write-Output 'Alta ADM6: HTTP 201'

    $consulta = Solicitar 'GET' $ruta
    if ($consulta.StatusCode -ne 200 -or ($consulta.Content | ConvertFrom-Json).valor -ne 'valor inicial de prueba') {
        throw "Consulta despues del alta: HTTP $($consulta.StatusCode)"
    }
    Write-Output 'Consulta ADM6: valor persistido'

    $edicion = Solicitar 'PUT' $ruta @{ idParametro = $id; valor = 'valor actualizado de prueba' }
    if ($edicion.StatusCode -ne 200) { throw "Edicion HTTP $($edicion.StatusCode)" }
    $consulta = Solicitar 'GET' $ruta
    if ($consulta.StatusCode -ne 200 -or ($consulta.Content | ConvertFrom-Json).valor -ne 'valor actualizado de prueba') {
        throw "Consulta despues de la edicion: HTTP $($consulta.StatusCode)"
    }
    Write-Output 'Edicion ADM6: valor actualizado y persistido'

    $baja = Solicitar 'DELETE' $ruta
    if ($baja.StatusCode -ne 200) { throw "Baja HTTP $($baja.StatusCode)" }
    $creado = $false
    $final = Solicitar 'GET' $ruta
    if ($final.StatusCode -ne 404) { throw "Consulta despues de la baja: HTTP $($final.StatusCode)" }
    Write-Output 'Baja ADM6: registro eliminado'

    $bitacora = Invoke-WebRequest -Uri 'http://127.0.0.1:5159/bitacora' -Headers @{
        Authorization = "Bearer $token"
    } -SkipHttpErrorCheck -TimeoutSec 15
    if ($bitacora.StatusCode -ne 200) { throw "Bitacora HTTP $($bitacora.StatusCode)" }
    $eventos = @($bitacora.Content | ConvertFrom-Json | Where-Object {
        $_.idBitacora -gt $ultimoEvento -and $_.descripcion -like "*$id*"
    })
    if ($eventos.Count -lt 3) { throw "Se esperaban tres eventos de bitacora; se encontraron $($eventos.Count)." }
    Write-Output "Bitacora ADM6: $($eventos.Count) eventos del parametro de prueba"
} finally {
    if ($creado -and $token) {
        try {
            $limpieza = Solicitar 'DELETE' $ruta
            Write-Output "Limpieza del parametro de prueba: HTTP $($limpieza.StatusCode)"
        } catch {
            Write-Output 'Limpieza pendiente: revise el parametro FABAUDIT.'
        }
    }
    $token = $null
}
