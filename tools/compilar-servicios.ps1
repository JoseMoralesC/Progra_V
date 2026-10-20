param([string]$Servicio, [switch]$SinRed)
$ErrorActionPreference = 'Stop'
$taskRoot = [IO.Path]::GetFullPath((Split-Path $PSScriptRoot -Parent))
$taskServices = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json
if ($Servicio) {
    $taskServices = @($taskServices | Where-Object name -eq $Servicio)
    if (!$taskServices.Count) { throw 'Servicio desconocido.' }
}
foreach ($taskService in $taskServices) {
    $taskDirectory = [IO.Path]::GetFullPath((Join-Path $taskRoot ('NuevoAvatar.' + $taskService.name)))
    $taskTarget = [IO.Path]::GetFullPath((Join-Path $taskDirectory 'target'))
    # Maven clean elimina recursivamente su target: verifico el destino antes de invocarlo.
    if (!$taskDirectory.StartsWith($taskRoot + '\', [StringComparison]::OrdinalIgnoreCase) -or
        !$taskTarget.StartsWith($taskDirectory + '\', [StringComparison]::OrdinalIgnoreCase)) {
        throw 'Ruta de compilacion fuera del proyecto.'
    }
    Write-Host ('Compilando ' + $taskService.name)
    Push-Location $taskDirectory
    try {
        if ($taskService.kind -eq 'java') {
            $taskArguments = @('-q', '-f', (Join-Path $taskRoot 'pom.xml'), '-pl', ('NuevoAvatar.' + $taskService.name), '-am', '-DskipTests', 'clean', 'package')
            if ($SinRed) { $taskArguments = @('-o') + $taskArguments }
            & .\mvnw.cmd @taskArguments
        } else {
            $taskProject = Get-ChildItem -LiteralPath $taskDirectory -Filter '*.csproj' | Select-Object -First 1
            & dotnet build $taskProject.FullName --nologo -v:q
        }
        if ($LASTEXITCODE -ne 0) { throw ('Fallo la compilacion de ' + $taskService.name) }
        if ($taskService.kind -eq 'java') {
            & python (Join-Path $PSScriptRoot 'validar-artefactos.py') --servicio $taskService.name
            if ($LASTEXITCODE -ne 0) { throw ('Artefacto invalido de ' + $taskService.name) }
        }
    } finally { Pop-Location }
}
Write-Host 'Compilacion completada. Las pruebas de integracion se ejecutan por separado.'
