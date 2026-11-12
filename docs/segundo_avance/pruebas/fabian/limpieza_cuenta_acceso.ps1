$ErrorActionPreference = 'Stop'

$raiz = (Resolve-Path -LiteralPath (Join-Path $PSScriptRoot '../../../..')).Path
$configuracion = @{}
foreach ($linea in Get-Content -LiteralPath (Join-Path $raiz '.env')) {
    if ($linea -match '^([A-Za-z_][A-Za-z0-9_]*)=(.*)$') {
        $configuracion[$matches[1]] = $matches[2].Trim('"', "'")
    }
}

foreach ($nombre in @('DB_HOST', 'DB_PORT', 'DB_USERNAME', 'DB_PASSWORD')) {
    if (-not $configuracion[$nombre]) { throw "Falta $nombre en el entorno local." }
}

$cadena = New-Object System.Data.SqlClient.SqlConnectionStringBuilder
$cadena['Data Source'] = "$($configuracion.DB_HOST),$($configuracion.DB_PORT)"
$cadena['Initial Catalog'] = 'PrograV_Autenticacion'
$cadena['User ID'] = $configuracion.DB_USERNAME
$cadena['Password'] = $configuracion.DB_PASSWORD
$cadena['Encrypt'] = $true
$cadena['TrustServerCertificate'] = $true
$cadena['Connect Timeout'] = 8

$consulta = (Get-Content -LiteralPath (Join-Path $PSScriptRoot 'limpieza_cuenta_acceso.sql') -Raw) `
    -replace '(?m)^GO\s*\r?\n', ''
$conexion = New-Object System.Data.SqlClient.SqlConnection($cadena.ConnectionString)
try {
    $conexion.Open()
    $comando = $conexion.CreateCommand()
    $comando.CommandText = $consulta
    $comando.CommandTimeout = 15
    [void]$comando.ExecuteNonQuery()

    $verificacion = $conexion.CreateCommand()
    $verificacion.CommandText = 'SELECT COUNT(*) FROM seguridad.refresh_token WHERE email_usuario = @correo'
    [void]$verificacion.Parameters.AddWithValue('@correo', 'fabian.acceso.auditoria@cuc.cr')
    $restantes = [int]$verificacion.ExecuteScalar()
    if ($restantes -ne 0) { throw "Permanecen $restantes tokens de la cuenta temporal." }
    Write-Output 'Referencias de sesion temporales eliminadas.'
} finally {
    $conexion.Dispose()
}
