# Uso la misma conexion local de PrograV que Seguridad, sin guardar claves aqui.
$ErrorActionPreference = 'Stop'
$taskConfigPath = Join-Path $PSScriptRoot '../seguridad/NuevoAvatarSeguridad/appsettings.Development.json'
if (!(Test-Path -LiteralPath $taskConfigPath)) {
    throw 'Configure primero la conexion SeguridadDb en appsettings.Development.json de Seguridad.'
}
$taskConfig = Get-Content -LiteralPath $taskConfigPath -Raw | ConvertFrom-Json
$taskConnection = [System.Data.Common.DbConnectionStringBuilder]::new()
$taskConnection.set_ConnectionString($taskConfig.ConnectionStrings.SeguridadDb)
if ($taskConnection.get_Item('Database') -ne 'PrograV') {
    throw 'SeguridadDb debe apuntar a PrograV antes de ejecutar estas pruebas.'
}
$taskPreviousUrl = $env:DB_URL
$taskPreviousUser = $env:DB_USER
$taskPreviousPassword = $env:DB_PASSWORD
try {
    $taskServer = [string]$taskConnection.get_Item('Server')
    $env:DB_URL = 'jdbc:sqlserver://' + $taskServer.Replace(',', ':') + ';databaseName=PrograV;encrypt=true;trustServerCertificate=true'
    $env:DB_USER = [string]$taskConnection.get_Item('User Id')
    $env:DB_PASSWORD = [string]$taskConnection.get_Item('Password')
    Push-Location $PSScriptRoot
    try { & .\mvnw.cmd spring-boot:run } finally { Pop-Location }
} finally {
    $env:DB_URL = $taskPreviousUrl
    $env:DB_USER = $taskPreviousUser
    $env:DB_PASSWORD = $taskPreviousPassword
}
