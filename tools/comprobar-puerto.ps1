param([Parameter(Mandatory = $true)][int]$Puerto)
# Compruebo si ya hay un servicio escuchando; no detengo procesos existentes.
$taskClient = [System.Net.Sockets.TcpClient]::new()
try {
    $taskConnect = $taskClient.ConnectAsync('127.0.0.1', $Puerto)
    if ($taskConnect.Wait(1000) -and $taskClient.Connected) {
        Write-Host "El puerto $Puerto ya esta ocupado. No inicio otra instancia."
        exit 1
    }
} catch {
    # Si la conexion es rechazada, considero el puerto disponible.
} finally { $taskClient.Dispose() }
exit 0
