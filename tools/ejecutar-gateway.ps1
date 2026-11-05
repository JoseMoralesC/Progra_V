param([switch]$Verificar)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path $PSScriptRoot -Parent
$taskProject = Join-Path $taskRoot 'NuevoAvatar.Gateway/NuevoAvatar.Gateway.csproj'
if ($Verificar) {
    if (!(Test-Path -LiteralPath $taskProject)) { throw 'Proyecto Gateway ausente.' }
    Write-Host 'Gateway: puerto 5080, sin BD ni clave interna. Destinos configurables con Destinos__Servicio.'
    exit 0
}
& (Join-Path $PSScriptRoot 'comprobar-puerto.ps1') -Puerto 5080
if ($LASTEXITCODE -ne 0) { exit 1 }
& dotnet run --project $taskProject --launch-profile http
exit $LASTEXITCODE
