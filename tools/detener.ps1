param([switch]$Verificar)
$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot 'servicios-comunes.ps1')
$operationLock = $null
try {
    if (!$Verificar) { $operationLock = Obtener-BloqueoServicios }
    $snapshot = Obtener-SnapshotProcesos
    $records = @(Obtener-RegistroProcesos)
    $candidates = @{}
    foreach ($record in $records) {
        if (Proceso-Coincide $record) { $candidates[[int]$record.pid] = $snapshot[[int]$record.pid] }
    }
    # Compatibilidad con servicios abiertos manualmente desde este repositorio.
    # El registro permite detener los procesos propios incluso si cambio el .env.
    try {
        $ports = @(Obtener-PuertosConfigurados | Select-Object -Unique)
        foreach ($listener in @(Get-NetTCPConnection -State Listen -ErrorAction Stop | Where-Object { $_.LocalPort -in $ports })) {
            $process = $snapshot[[int]$listener.OwningProcess]
            if ($process -and (Pertenece-Directorio $process $serviceRoot $snapshot)) { $candidates[[int]$process.ProcessId] = $process }
        }
    } catch { Write-Host 'No se pudieron revisar todos los puertos del .env; se usara el registro de procesos propios.' -ForegroundColor Yellow }
    # Proteger esta consola y sus ancestros, ademas de servicios ajenos a la app.
    $protected = @{}
    $cursor = $snapshot[$PID]
    while ($cursor -and !$protected.ContainsKey([int]$cursor.ProcessId)) {
        $protected[[int]$cursor.ProcessId] = $true
        $cursor = $snapshot[[int]$cursor.ParentProcessId]
    }
    $allowed = @('java.exe', 'javaw.exe', 'dotnet.exe', 'node.exe', 'powershell.exe', 'pwsh.exe', 'cmd.exe')
    foreach ($process in @($candidates.Values)) {
        if ($process.Name -notin $allowed -and !([string]$process.ExecutablePath).StartsWith($serviceRoot + '\', [StringComparison]::OrdinalIgnoreCase)) { $candidates.Remove([int]$process.ProcessId) }
    }
    # Incluir hijos de cada runtime propio para no dejar trabajadores de Vite vivos.
    $depths = @{}
    foreach ($id in @($candidates.Keys)) { $depths[$id] = 0 }
    do {
        $added = $false
        foreach ($process in $snapshot.Values) {
            $id = [int]$process.ProcessId; $parentId = [int]$process.ParentProcessId
            if ($candidates.ContainsKey($parentId) -and !$candidates.ContainsKey($id) -and !$protected.ContainsKey($id) -and
                $process.CreationDate -ge $candidates[$parentId].CreationDate -and
                ($process.Name -in $allowed -or ([string]$process.ExecutablePath).StartsWith($serviceRoot + '\', [StringComparison]::OrdinalIgnoreCase))) {
                $candidates[$id] = $process; $depths[$id] = $depths[$parentId] + 1; $added = $true
            }
        }
    } while ($added)
    $failed = $false
    foreach ($id in @($candidates.Keys | Sort-Object { $depths[$_] } -Descending)) {
        if ($protected.ContainsKey($id)) { continue }
        $original = $candidates[$id]
        $current = Get-CimInstance Win32_Process -Filter "ProcessId = $id" -ErrorAction SilentlyContinue
        if (!$current -or $current.CreationDate -ne $original.CreationDate) { continue }
        if ($Verificar) { Write-Host "Se detendria $($current.Name), PID $id."; continue }
        try {
            $handle = Get-Process -Id $id -ErrorAction SilentlyContinue
            Stop-Process -Id $id -ErrorAction Stop
            if ($handle -and !$handle.WaitForExit(5000)) { throw 'El proceso aun no confirma su cierre.' }
            Write-Host "Detenido $($current.Name), PID $id."
        }
        catch { if (Get-Process -Id $id -ErrorAction SilentlyContinue) { Write-Host "No se pudo detener PID $id." -ForegroundColor Red; $failed = $true } }
    }
    if (!$Verificar) {
        $remaining = @($records | Where-Object { Proceso-Coincide $_ })
        if ($remaining.Count) { Guardar-RegistroProcesos $remaining }
        elseif (Test-Path -LiteralPath $serviceStateFile) { Remove-Item -LiteralPath $serviceStateFile -Force }
    }
    if (!$candidates.Count) { Write-Host 'No se encontraron procesos del proyecto para detener.' }
    if ($Verificar) { Write-Host 'Verificacion terminada. No se detuvo ningun proceso ni se modifico el registro.' }
    if ($failed) { exit 1 }
    exit 0
} catch { Write-Host ("[ERROR] " + $_.Exception.Message) -ForegroundColor Red; exit 1 }
finally { if ($operationLock) { $operationLock.ReleaseMutex(); $operationLock.Dispose() } }
