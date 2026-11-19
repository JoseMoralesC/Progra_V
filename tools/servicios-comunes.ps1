. (Join-Path $PSScriptRoot 'urls-entorno.ps1')
$script:serviceRoot = [IO.Path]::GetFullPath((Split-Path $PSScriptRoot -Parent))
$script:serviceRuntime = Join-Path $serviceRoot 'outputs\ejecucion'
$script:serviceStateFile = Join-Path $serviceRuntime 'procesos.json'

function Obtener-SnapshotProcesos {
    $snapshot = @{}
    Get-CimInstance Win32_Process -ErrorAction Stop | ForEach-Object { $snapshot[[int]$_.ProcessId] = $_ }
    return $snapshot
}

function Pertenece-Directorio($Proceso, [string]$Directorio, [hashtable]$Snapshot) {
    $prefix = [IO.Path]::GetFullPath($Directorio).TrimEnd('\') + '\'
    $cursor = $Proceso
    for ($depth = 0; $cursor -and $depth -lt 8; $depth++) {
        if (([string]$cursor.ExecutablePath).StartsWith($prefix, [StringComparison]::OrdinalIgnoreCase) -or
            ([string]$cursor.CommandLine).IndexOf($prefix, [StringComparison]::OrdinalIgnoreCase) -ge 0) { return $true }
        $parent = $Snapshot[[int]$cursor.ParentProcessId]
        if ($parent -and $parent.CreationDate -gt $cursor.CreationDate) { break }
        $cursor = $parent
    }
    return $false
}

function Obtener-RegistroProcesos {
    if (!(Test-Path -LiteralPath $serviceStateFile)) { return @() }
    $state = Get-Content -LiteralPath $serviceStateFile -Raw | ConvertFrom-Json
    if ($state.raiz -ne $serviceRoot) { throw 'El registro de procesos pertenece a otro directorio.' }
    return @($state.procesos)
}

function Proceso-Coincide($Registro) {
    $process = Get-Process -Id ([int]$Registro.pid) -ErrorAction SilentlyContinue
    if (!$process) { return $false }
    try { return $process.StartTime.ToUniversalTime().Ticks.ToString() -eq [string]$Registro.inicio }
    catch { return $false }
}

function Guardar-RegistroProcesos([array]$Registros) {
    New-Item -ItemType Directory -Path $serviceRuntime -Force | Out-Null
    $state = @{ raiz = $serviceRoot; procesos = @($Registros) } | ConvertTo-Json -Depth 5
    $temporary = Join-Path $serviceRuntime 'procesos.json.tmp'
    [IO.File]::WriteAllText($temporary, $state, [Text.UTF8Encoding]::new($false))
    Move-Item -LiteralPath $temporary -Destination $serviceStateFile -Force
}

function Obtener-BloqueoServicios {
    $hash = [Security.Cryptography.SHA256]::Create()
    try { $id = [BitConverter]::ToString($hash.ComputeHash([Text.Encoding]::UTF8.GetBytes($serviceRoot.ToLowerInvariant()))).Replace('-', '') }
    finally { $hash.Dispose() }
    $mutex = [Threading.Mutex]::new($false, ('Local\NuevoAvatar-' + $id))
    try { $locked = $mutex.WaitOne(0) }
    catch [Threading.AbandonedMutexException] { $locked = $true }
    if (!$locked) { $mutex.Dispose(); throw 'Ya hay un arranque o una parada del proyecto en curso.' }
    return $mutex
}

function Obtener-PuertosConfigurados {
    Cargar-EntornoLocal
    $manifest = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json
    foreach ($entry in $manifest) { (Obtener-OrigenServicio $entry.name).Port }
    foreach ($name in @('GATEWAY', 'FRONTEND', 'JOSE_FRONTEND', 'RAMSES_FRONTEND', 'ESTUDIANTE_WEB', 'USUARIO_WEB')) {
        if ([Environment]::GetEnvironmentVariable($name + '_BASE_URL')) { (Obtener-OrigenServicio $name).Port }
    }
    foreach ($name in @('CURSO', 'DIRECCION', 'GRUPO', 'PERIODO', 'ESTUDIANTE_WEB', 'USUARIO_WEB')) {
        if ([Environment]::GetEnvironmentVariable($name + '_HTTPS_BASE_URL')) { (Obtener-OrigenServicio $name -Https).Port }
    }
}
