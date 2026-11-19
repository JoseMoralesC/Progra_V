param([Parameter(Mandatory = $true)][int]$Puerto, [string]$HostServicio = '127.0.0.1')
# Compruebo si ya hay un servicio escuchando; no detengo procesos existentes.
$taskClient = [System.Net.Sockets.TcpClient]::new()
try {
    $taskConnect = $taskClient.ConnectAsync($HostServicio, $Puerto)
    if ($taskConnect.Wait(1000) -and $taskClient.Connected) {
        Write-Host "El puerto $Puerto ya esta ocupado. No inicio otra instancia."
        exit 1
    }
} catch {
    # Si la conexion es rechazada, considero el puerto disponible.
} finally { $taskClient.Dispose() }
exit 0
