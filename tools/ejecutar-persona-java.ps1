param(
    [Parameter(Mandatory = $true)]
    [ValidateSet(2, 4, 5)]
    [int]$Persona,
    [switch]$Verificar
)

$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path $PSScriptRoot -Parent
$taskService = Join-Path $taskRoot "services/persona$Persona"
$taskWrapper = Join-Path $taskRoot 'services/persona2/mvnw.cmd'
$taskLocalConfig = Join-Path $taskRoot 'services/seguridad/NuevoAvatarSeguridad/appsettings.Development.json'
$taskEnvPath = Join-Path $taskRoot '.env'

# Leo solo la conexion SQL del .env; no ejecuto su contenido ni muestro claves.
$taskValues = @{}
if (Test-Path -LiteralPath $taskEnvPath) {
    foreach ($taskLine in Get-Content -LiteralPath $taskEnvPath) {
        if ($taskLine -match '^\s*(?:export\s+)?(DB_HOST|DB_PORT|DB_NAME|DB_USERNAME|DB_PASSWORD)\s*=\s*(.*?)\s*$') {
            $taskKey = $matches[1]
            $taskValue = $matches[2]
            if ($taskValue.Length -ge 2 -and
                (($taskValue.StartsWith('"') -and $taskValue.EndsWith('"')) -or
                 ($taskValue.StartsWith("'") -and $taskValue.EndsWith("'")))) {
                $taskValue = $taskValue.Substring(1, $taskValue.Length - 2)
            }
            $taskValues[$taskKey] = $taskValue
        }
    }
}
# Respeto las variables ya definidas en la consola, como hace Seguridad.
foreach ($taskKey in @('DB_HOST', 'DB_PORT', 'DB_NAME', 'DB_USERNAME', 'DB_PASSWORD')) {
    $taskValue = [Environment]::GetEnvironmentVariable($taskKey, 'Process')
    if (![string]::IsNullOrWhiteSpace($taskValue)) { $taskValues[$taskKey] = $taskValue }
}

$taskConnection = [System.Data.Common.DbConnectionStringBuilder]::new()
$taskCompleteEnv = $true
foreach ($taskKey in @('DB_HOST', 'DB_NAME', 'DB_USERNAME', 'DB_PASSWORD')) {
    if ([string]::IsNullOrWhiteSpace($taskValues[$taskKey])) { $taskCompleteEnv = $false }
}
if ($taskCompleteEnv) {
    $taskPort = if ($taskValues['DB_PORT']) { $taskValues['DB_PORT'] } else { '1433' }
    $taskConnection.set_Item('Server', "$($taskValues['DB_HOST']),$taskPort")
    $taskConnection.set_Item('Database', $taskValues['DB_NAME'])
    $taskConnection.set_Item('User Id', $taskValues['DB_USERNAME'])
    $taskConnection.set_Item('Password', $taskValues['DB_PASSWORD'])
} elseif (Test-Path -LiteralPath $taskLocalConfig) {
    $taskConfig = Get-Content -LiteralPath $taskLocalConfig -Raw | ConvertFrom-Json
    $taskConnection.set_ConnectionString($taskConfig.ConnectionStrings.SeguridadDb)
} else {
    throw 'Configura DB_HOST, DB_NAME, DB_USERNAME y DB_PASSWORD en .env o SeguridadDb en el archivo local de Seguridad.'
}
if ($taskConnection.get_Item('Database') -ne 'PrograV') {
    throw 'La conexion debe apuntar a PrograV. Revisa la configuracion local sin publicar las claves.'
}
if (!(Test-Path -LiteralPath $taskWrapper) -or !(Test-Path -LiteralPath (Join-Path $taskService 'pom.xml'))) {
    throw 'Falta el wrapper Maven o el proyecto. Mantiene los .bat en la raiz del repositorio.'
}
$taskJava = Get-Command java -ErrorAction SilentlyContinue
if (!$taskJava) { throw 'No encuentro Java. Instala/configura Java 21.' }

$taskServer = ([string]$taskConnection.get_Item('Server')).Replace(',', ':')
$taskJdbcUrl = "jdbc:sqlserver://$taskServer;databaseName=PrograV;encrypt=true;trustServerCertificate=true"
$taskVariables = @{}
if ($Persona -eq 5) {
    $taskVariables['SPRING_DATASOURCE_URL'] = $taskJdbcUrl
    $taskVariables['SPRING_DATASOURCE_USERNAME'] = [string]$taskConnection.get_Item('User Id')
    $taskVariables['SPRING_DATASOURCE_PASSWORD'] = [string]$taskConnection.get_Item('Password')
} else {
    $taskVariables['DB_URL'] = $taskJdbcUrl
    $taskVariables['DB_USER'] = [string]$taskConnection.get_Item('User Id')
    $taskVariables['DB_PASSWORD'] = [string]$taskConnection.get_Item('Password')
}
if ($Verificar) {
    Write-Host "Persona ${Persona}: configuracion local y proyecto verificados. Base: PrograV. No inicio servicios."
    exit 0
}

$taskHttpPort = switch ($Persona) { 2 { 8082 } 4 { 8084 } 5 { 8085 } }
if ($env:SERVER_PORT) { $taskHttpPort = [int]$env:SERVER_PORT }
& (Join-Path $PSScriptRoot 'comprobar-puerto.ps1') -Puerto $taskHttpPort
if ($LASTEXITCODE -ne 0) { exit 1 }

# Configuro las claves solo en este proceso y restauro el ambiente al terminar.
$taskPrevious = @{}
foreach ($taskKey in $taskVariables.Keys) {
    $taskPrevious[$taskKey] = [Environment]::GetEnvironmentVariable($taskKey, 'Process')
    [Environment]::SetEnvironmentVariable($taskKey, $taskVariables[$taskKey], 'Process')
}
try {
    Push-Location $taskService
    try {
        & $taskWrapper -f pom.xml spring-boot:run
        $taskExitCode = $LASTEXITCODE
    } finally { Pop-Location }
} finally {
    foreach ($taskKey in $taskPrevious.Keys) {
        [Environment]::SetEnvironmentVariable($taskKey, $taskPrevious[$taskKey], 'Process')
    }
}
exit $taskExitCode
