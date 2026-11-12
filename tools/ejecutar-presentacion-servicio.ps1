param([Parameter(Mandatory = $true)][string]$Servicio, [switch]$ModuloLocal)
$ErrorActionPreference = 'Stop'
$raiz = [IO.Path]::GetFullPath((Split-Path $PSScriptRoot -Parent))
$manifiesto = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json
$entrada = $manifiesto | Where-Object name -eq $Servicio
if (!$entrada) { throw "Servicio desconocido: $Servicio" }

foreach ($linea in Get-Content (Join-Path $raiz '.env')) {
    if ($linea -match '^\s*(?:export\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*?)\s*$') {
        $nombre = $matches[1]; $valor = $matches[2]
        if ($valor.Length -ge 2 -and (($valor.StartsWith('"') -and $valor.EndsWith('"')) -or ($valor.StartsWith("'") -and $valor.EndsWith("'")))) {
            $valor = $valor.Substring(1, $valor.Length - 2)
        }
        [Environment]::SetEnvironmentVariable($nombre, $valor, 'Process')
    }
}

$env:SERVICE_DB_NAME = [string]$entrada.database
$env:SERVER_PORT = [string]$entrada.port
$env:SEGURIDAD_URL = 'http://localhost:5158'
$env:SEGURIDAD_BASE_URL = $env:SEGURIDAD_URL
$env:BITACORA_URL = 'http://localhost:5159'
$env:MATRICULA_BASE_URL = 'http://localhost:8085'
$env:NOTAS_BASE_URL = 'http://localhost:8092'

if ($Servicio -eq 'Modulo' -and $ModuloLocal) {
    # Evito que DotNetEnv vuelva a cargar la conexion remota y uso autenticacion de Windows local.
    $env:DB_HOST = ' '; $env:DB_USERNAME = ' '; $env:DB_PASSWORD = ' '
    $env:SERVICE_DB_USER_MODULO = ' '; $env:SERVICE_DB_PASSWORD_MODULO = ' '
    $env:ConnectionStrings__SeguridadDb = 'Server=.\SQLEXPRESS;Database=PrograV_Modulo;Trusted_Connection=True;TrustServerCertificate=True'
} else {
    $usuarioServicio = [Environment]::GetEnvironmentVariable(('SERVICE_DB_USER_' + $Servicio.ToUpperInvariant()), 'Process')
    $claveServicio = [Environment]::GetEnvironmentVariable(('SERVICE_DB_PASSWORD_' + $Servicio.ToUpperInvariant()), 'Process')
    if ($usuarioServicio -and $claveServicio) { $env:DB_USERNAME = $usuarioServicio; $env:DB_PASSWORD = $claveServicio }
    $env:DB_URL = "jdbc:sqlserver://$($env:DB_HOST):$($env:DB_PORT);databaseName=$($env:SERVICE_DB_NAME);encrypt=true;trustServerCertificate=true"
    $env:DB_USER = $env:DB_USERNAME
    $env:SPRING_DATASOURCE_URL = $env:DB_URL
    $env:SPRING_DATASOURCE_USERNAME = $env:DB_USER
    $env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
}

$directorio = Join-Path $raiz ('NuevoAvatar.' + $Servicio)
Push-Location $directorio
try {
    if ($entrada.kind -eq 'java') {
        $jar = Get-ChildItem (Join-Path $directorio 'target') -Filter '*.jar' | Select-Object -First 1
        if (!$jar) { throw "Falta compilar $Servicio." }
        & java -Xms32m -Xmx192m -jar $jar.FullName
    } else {
        $proyecto = Get-ChildItem $directorio -Filter '*.csproj' | Select-Object -First 1
        & dotnet run --no-build --no-restore --project $proyecto.FullName --launch-profile http
    }
} finally { Pop-Location }
exit $LASTEXITCODE
