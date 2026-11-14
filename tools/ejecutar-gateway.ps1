param([switch]$Verificar)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path $PSScriptRoot -Parent
$taskProject = Join-Path $taskRoot 'NuevoAvatar.Gateway/NuevoAvatar.Gateway.csproj'
. (Join-Path $PSScriptRoot 'urls-entorno.ps1')
Cargar-EntornoLocal
$gatewayOrigin = Obtener-OrigenServicio 'Gateway'
if ($Verificar) {
    if (!(Test-Path -LiteralPath $taskProject)) { throw 'Proyecto Gateway ausente.' }
    Write-Host 'Gateway: origen y destinos configurados mediante variables *_BASE_URL del .env raiz.'
    exit 0
}
& (Join-Path $PSScriptRoot 'comprobar-puerto.ps1') -Puerto $gatewayOrigin.Port -HostServicio $gatewayOrigin.Host
if ($LASTEXITCODE -ne 0) { exit 1 }
& dotnet run --project $taskProject --launch-profile http
exit $LASTEXITCODE
