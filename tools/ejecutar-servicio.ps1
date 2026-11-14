param([Parameter(Mandatory = $true)][string]$Servicio, [switch]$Verificar)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path $PSScriptRoot -Parent
$taskManifest = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json
$taskEntry = $taskManifest | Where-Object { $_.name -eq $Servicio }
if (!$taskEntry) { throw 'Servicio desconocido.' }
$taskDirectory = Join-Path $taskRoot "NuevoAvatar.$Servicio"
. (Join-Path $PSScriptRoot 'urls-entorno.ps1')
Cargar-EntornoLocal
$taskOrigin = Obtener-OrigenServicio $Servicio
if (!$env:DB_HOST -or !$env:DB_NAME -or !$env:DB_USERNAME -or !$env:DB_PASSWORD) {
    throw 'Falta configurar DB_HOST, DB_NAME, DB_USERNAME o DB_PASSWORD en el .env local.'
}
if (!$env:DB_PORT) { $env:DB_PORT = '1433' }
# DB_NAME conserva el origen de la migracion. Cada proceso utiliza su destino.
$env:SERVICE_DB_NAME = [string]$taskEntry.database
$taskUser = [Environment]::GetEnvironmentVariable(('SERVICE_DB_USER_' + $Servicio.ToUpperInvariant()), 'Process')
$taskPassword = [Environment]::GetEnvironmentVariable(('SERVICE_DB_PASSWORD_' + $Servicio.ToUpperInvariant()), 'Process')
if ($taskUser -and $taskPassword) {
    $env:DB_USERNAME = $taskUser
    $env:DB_PASSWORD = $taskPassword
} else {
    [Environment]::SetEnvironmentVariable(('SERVICE_DB_USER_' + $Servicio.ToUpperInvariant()), $env:DB_USERNAME, 'Process')
    [Environment]::SetEnvironmentVariable(('SERVICE_DB_PASSWORD_' + $Servicio.ToUpperInvariant()), $env:DB_PASSWORD, 'Process')
}
if (!$env:SERVICES_INTERNAL_KEY -or $env:SERVICES_INTERNAL_KEY.Length -lt 32) {
    throw 'Configura SERVICES_INTERNAL_KEY (minimo 32 caracteres) para las comunicaciones internas.'
}
$env:DB_URL = "jdbc:sqlserver://$($env:DB_HOST):$($env:DB_PORT);databaseName=$($env:SERVICE_DB_NAME);encrypt=true;trustServerCertificate=true"
$env:DB_USER = $env:DB_USERNAME
$env:SPRING_DATASOURCE_URL = $env:DB_URL
$env:SPRING_DATASOURCE_USERNAME = $env:DB_USER
$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
# Los destinos se conservan tal como se configuraron en el entorno o .env.
$env:SERVER_PORT = [string]$taskOrigin.Port
if ($Verificar) { Write-Host "$Servicio : base $($taskEntry.database), puerto $($taskOrigin.Port), configuracion verificada. No inicio procesos."; exit 0 }
& (Join-Path $PSScriptRoot 'comprobar-puerto.ps1') -Puerto $taskOrigin.Port -HostServicio $taskOrigin.Host
if ($LASTEXITCODE -ne 0) { exit 1 }
Push-Location $taskDirectory
try {
    if ($taskEntry.kind -eq 'java') {
        & python (Join-Path $PSScriptRoot 'validar-artefactos.py') --servicio $Servicio
        if ($LASTEXITCODE -ne 0) { throw 'Compila el servicio con tools/compilar-servicios.ps1 antes de arrancarlo.' }
        $taskJar = Get-ChildItem -LiteralPath (Join-Path $taskDirectory 'target') -Filter '*.jar' | Select-Object -First 1
        & java -Xms32m -Xmx192m -jar $taskJar.FullName
    } else {
        $taskProject = Get-ChildItem -LiteralPath $taskDirectory -Filter "*.csproj" | Select-Object -First 1
        & dotnet run --project $taskProject.FullName --launch-profile http
    }
    $taskExit = $LASTEXITCODE
} finally { Pop-Location }
exit $taskExit
