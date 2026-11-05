param([switch]$Verificar)
$ErrorActionPreference = 'Stop'
$taskRoot = [IO.Path]::GetFullPath((Split-Path $PSScriptRoot -Parent))
$taskPorts = @(5158, 7452, 7192, 8082, 8084, 8085, 5255, 5227, 5256, 5262, 7082, 7264, 7257, 7253)
$taskPorts += @(5159, 5160, 5161, 5162, 8086, 8087, 8088, 8089, 8090, 8091, 8092, 8093, 8094, 8095)
$taskPorts += @(5080)
try {
    # Identifico los procesos y sus padres sin mostrar argumentos ni claves.
    $taskProcesses = @{}
    Get-CimInstance Win32_Process | ForEach-Object { $taskProcesses[[int]$_.ProcessId] = $_ }
    $taskListeners = @(Get-NetTCPConnection -State Listen -ErrorAction Stop |
        Where-Object { $_.LocalPort -in $taskPorts } |
        Sort-Object OwningProcess -Unique)
    foreach ($taskListener in $taskListeners) {
        $taskProcessId = [int]$taskListener.OwningProcess
        $taskProcess = $taskProcesses[$taskProcessId]
        $taskBelongs = $false
        $taskCursor = $taskProcess
        # Compruebo la ruta del repositorio en el proceso o sus lanzadores.
        for ($taskDepth = 0; $taskCursor -and $taskDepth -lt 6; $taskDepth++) {
            if (([string]$taskCursor.CommandLine).IndexOf($taskRoot, [StringComparison]::OrdinalIgnoreCase) -ge 0 -or
                ([string]$taskCursor.ExecutablePath).StartsWith($taskRoot + '\', [StringComparison]::OrdinalIgnoreCase)) {
                $taskBelongs = $true
                break
            }
            $taskCursor = $taskProcesses[[int]$taskCursor.ParentProcessId]
        }
        if (!$taskBelongs) {
            Write-Host "Puerto $($taskListener.LocalPort): no puedo confirmar que pertenezca al proyecto; no lo detengo."
            continue
        }
        if ($Verificar) {
            Write-Host "Detendria servicio del proyecto en puerto $($taskListener.LocalPort), PID $taskProcessId."
        } else {
            # Reviso la identidad nuevamente para evitar detener un PID reutilizado.
            $taskCurrent = Get-CimInstance Win32_Process -Filter "ProcessId = $taskProcessId"
            if ($taskCurrent -and $taskCurrent.CreationDate -eq $taskProcess.CreationDate) {
                Stop-Process -Id $taskProcessId -ErrorAction Stop
                Write-Host "Detenido: puerto $($taskListener.LocalPort), PID $taskProcessId."
            }
        }
    }
    if (!$taskListeners.Count) { Write-Host 'No hay servicios escuchando en los puertos del proyecto.' }
    if (!$Verificar) { Write-Host 'Finalizado. No detuve SQL Server ni Tailscale.' }
} catch {
    Write-Host 'No pude completar la operacion. Si es un problema de permisos, ejecuta el .bat como administrador.'
    exit 1
}
