$root = $PSScriptRoot

$started = New-Object System.Collections.ArrayList
$omitted = New-Object System.Collections.ArrayList
$failed = New-Object System.Collections.ArrayList
$dotnetServices = New-Object System.Collections.ArrayList
$javaServices = New-Object System.Collections.ArrayList

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   NUEVO AVATAR - INICIO DE SERVICIOS" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$projects = Get-ChildItem $root -Directory | Where-Object {
    $_.Name -like "NuevoAvatar.*" -and
    $_.Name -notmatch '\.Tests$|\.Integracion$|\.Seguridad(\.|$)'
}

# Detectar proyectos y omitir los servicios cuyo puerto ya está activo.
foreach ($project in $projects) {
    $folder = $project.FullName
    $name = $project.Name
    $properties = Join-Path $folder "src\main\resources\application.properties"
    $service = $null

    if (Test-Path (Join-Path $folder "mvnw.cmd")) {
        $port = $null
        if (Test-Path $properties) {
            $match = Select-String -Path $properties -Pattern '^\s*server\.port\s*=\s*(\d+)' | Select-Object -First 1
            if ($match -and $match.Matches[0].Groups[1].Value) {
                $port = [int]$match.Matches[0].Groups[1].Value
            }
        }

        $service = [PSCustomObject]@{
            Name = $name
            Folder = $folder
            Port = $port
            ProjectFile = $null
            Kind = "Java"
        }
    }
    else {
        $csproj = Get-ChildItem $folder -Filter "*.csproj" -File |
            Where-Object { $_.Name -notmatch '\.Tests\.csproj$' } |
            Select-Object -First 1

        if ($csproj) {
            $port = $null
            $launchSettings = Join-Path $folder "Properties\launchSettings.json"
            if (Test-Path $launchSettings) {
                try {
                    $settings = Get-Content $launchSettings -Raw | ConvertFrom-Json
                    foreach ($profile in $settings.profiles.PSObject.Properties) {
                        if ($profile.Value.applicationUrl -match 'https?://[^:]+:(\d+)') {
                            $port = [int]$Matches[1]
                            break
                        }
                    }
                }
                catch {
                    $port = $null
                }
            }

            $service = [PSCustomObject]@{
                Name = $name
                Folder = $folder
                Port = $port
                ProjectFile = $csproj.FullName
                Kind = ".NET"
            }
        }
    }

    if (-not $service) {
        $reason = "$name - No se encontró proyecto ejecutable."
        Write-Host "[OMITIDO] $reason" -ForegroundColor DarkGray
        [void]$omitted.Add($reason)
        continue
    }

    if ($service.Port) {
        $listener = Get-NetTCPConnection -State Listen -LocalPort $service.Port -ErrorAction SilentlyContinue |
            Select-Object -First 1

        if ($listener) {
            $reason = "$name - Puerto $($service.Port) ya está ocupado."
            Write-Host "[OMITIDO] $reason" -ForegroundColor Yellow
            [void]$omitted.Add($reason)
            continue
        }
    }

    if ($service.Kind -eq "Java") {
        [void]$javaServices.Add($service)
    }
    else {
        [void]$dotnetServices.Add($service)
    }
}

# Mantener el mecanismo Java/Maven: una terminal y spring-boot:run por servicio.
foreach ($service in $javaServices) {
    $command = 'cd /d "' + $service.Folder + '" && .\mvnw.cmd spring-boot:run'
    try {
        Write-Host "[INICIANDO] $($service.Name) (Java/Maven)" -ForegroundColor Green
        Start-Process "cmd.exe" -ArgumentList "/k", $command
        [void]$started.Add("$($service.Name) (Java/Maven)")
        Start-Sleep -Milliseconds 500
    }
    catch {
        $reason = "$($service.Name) - No se pudo abrir la terminal Java: $($_.Exception.Message)"
        Write-Host "[FALLÓ] $reason" -ForegroundColor Red
        [void]$failed.Add($reason)
    }
}

# Compilar todos los proyectos .NET en serie. No se inicia ninguno hasta que
# hayan terminado todos los intentos de compilación.
$compiledDotnet = New-Object System.Collections.ArrayList
$dotnetAvailable = Get-Command dotnet -ErrorAction SilentlyContinue

foreach ($service in $dotnetServices) {
    if (-not $dotnetAvailable) {
        $reason = "$($service.Name) - No se encontró el comando dotnet."
        Write-Host "[FALLÓ] $reason" -ForegroundColor Red
        [void]$failed.Add($reason)
        continue
    }

    Write-Host "[COMPILANDO] $($service.Name)" -ForegroundColor Cyan
    try {
        & dotnet build $service.ProjectFile
        $buildExitCode = $LASTEXITCODE

        if ($buildExitCode -ne 0) {
            $reason = "$($service.Name) - dotnet build terminó con código $buildExitCode. No se iniciará."
            Write-Host "[FALLÓ] $reason" -ForegroundColor Red
            [void]$failed.Add($reason)
            continue
        }

        [void]$compiledDotnet.Add($service)
        Write-Host "[COMPILADO] $($service.Name)" -ForegroundColor Green
    }
    catch {
        $reason = "$($service.Name) - Error al compilar: $($_.Exception.Message). No se iniciará."
        Write-Host "[FALLÓ] $reason" -ForegroundColor Red
        [void]$failed.Add($reason)
    }
}

# Iniciar solo los .NET compilados, sin volver a compilar en sus terminales.
foreach ($service in $compiledDotnet) {
    $command = 'cd /d "' + $service.Folder + '" && dotnet run --no-build --no-restore --project "' + $service.ProjectFile + '"'
    try {
        Write-Host "[INICIANDO] $($service.Name) (.NET, sin recompilar)" -ForegroundColor Green
        Start-Process "cmd.exe" -ArgumentList "/k", $command
        [void]$started.Add("$($service.Name) (.NET)")
        Start-Sleep -Milliseconds 500
    }
    catch {
        $reason = "$($service.Name) - Compilado, pero no se pudo abrir la terminal: $($_.Exception.Message)"
        Write-Host "[FALLÓ] $reason" -ForegroundColor Red
        [void]$failed.Add($reason)
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "             RESUMEN FINAL" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nLanzados (terminal abierta): $($started.Count)" -ForegroundColor Green
if ($started.Count -eq 0) { Write-Host "  Ninguno" -ForegroundColor DarkGray }
foreach ($item in $started) { Write-Host "  - $item" -ForegroundColor Green }

Write-Host "`nOmitidos: $($omitted.Count)" -ForegroundColor Yellow
if ($omitted.Count -eq 0) { Write-Host "  Ninguno" -ForegroundColor DarkGray }
foreach ($item in $omitted) { Write-Host "  - $item" -ForegroundColor Yellow }

Write-Host "`nFallidos: $($failed.Count)" -ForegroundColor Red
if ($failed.Count -eq 0) { Write-Host "  Ninguno" -ForegroundColor DarkGray }
foreach ($item in $failed) { Write-Host "  - $item" -ForegroundColor Red }

Write-Host "`nEl estado 'Lanzados' indica que se abrió una terminal; no confirma que la aplicación haya completado su inicio." -ForegroundColor Cyan
Read-Host "Presiona Enter para cerrar esta ventana"
