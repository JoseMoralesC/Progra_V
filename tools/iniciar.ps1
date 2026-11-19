param(
    [switch]$Verificar,
    [switch]$SinCompilar,
    [switch]$SinFrontend,
    [switch]$ModuloLocal,
    [ValidateRange(1, 600)][int]$TiempoEspera = 120
)
$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot 'servicios-comunes.ps1')
$operationLock = $null
try {
    Cargar-EntornoLocal
    $manifest = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json
    $localHosts = @('localhost', '127.0.0.1', '::1', '[::1]', '0.0.0.0', '::', '[::]', $env:COMPUTERNAME)
    $localHosts += @(Get-NetIPAddress -ErrorAction Stop | Select-Object -ExpandProperty IPAddress)
    $entries = @($manifest) + @([pscustomobject]@{ name = 'Gateway'; kind = 'dotnet'; database = '' })
    if (!$SinFrontend) { $entries += [pscustomobject]@{ name = 'Frontend'; kind = 'node'; database = '' } }
    $targets = @()
    foreach ($entry in $entries) {
        $origin = Obtener-OrigenServicio $entry.name
        $directory = if ($entry.kind -eq 'node') { Join-Path $serviceRoot 'NuevoAvatar.Estudiante.React' } else { Join-Path $serviceRoot ('NuevoAvatar.' + $entry.name) }
        if (!(Test-Path -LiteralPath $directory)) { throw "Falta el directorio de $($entry.name)." }
        $project = $null; $artifact = $null
        if ($entry.kind -eq 'dotnet') {
            $project = Get-ChildItem -LiteralPath $directory -Filter '*.csproj' | Select-Object -First 1
            if (!$project) { throw "Falta el proyecto .NET de $($entry.name)." }
            [xml]$projectXml = Get-Content -LiteralPath $project.FullName -Raw
            $framework = @($projectXml.Project.PropertyGroup.TargetFramework | Where-Object { $_ })[0]
            $assembly = @($projectXml.Project.PropertyGroup.AssemblyName | Where-Object { $_ }) | Select-Object -First 1
            if (!$assembly) { $assembly = $project.BaseName }
            $artifact = Join-Path $directory "bin\Debug\$framework\$assembly.dll"
        } elseif ($entry.kind -eq 'node') {
            $artifact = Join-Path $directory 'node_modules\vite\bin\vite.js'
            if (!(Test-Path -LiteralPath $artifact)) { throw 'Faltan las dependencias del frontend. Ejecuta npm ci en NuevoAvatar.Estudiante.React.' }
        }
        $targets += [pscustomobject]@{
            name = $entry.name; kind = $entry.kind; database = $entry.database
            origin = $origin; directory = $directory; project = $project; artifact = $artifact
            local = $origin.Host -in $localHosts; estado = 'PENDIENTE'; proceso = $null
        }
    }
    if (!$Verificar) { $operationLock = Obtener-BloqueoServicios }
    $snapshot = Obtener-SnapshotProcesos
    $listeners = @(Get-NetTCPConnection -State Listen -ErrorAction Stop)
    $knownRecords = @(Obtener-RegistroProcesos | Where-Object { Proceso-Coincide $_ })
    $problems = @()
    foreach ($target in $targets) {
        if (!$target.local) { $target.estado = 'EXTERNO'; continue }
        $owners = @($listeners | Where-Object LocalPort -eq $target.origin.Port | Select-Object -ExpandProperty OwningProcess -Unique)
        if ($owners.Count) {
            $belongs = $true
            foreach ($owner in $owners) {
                if (!(Pertenece-Directorio $snapshot[[int]$owner] $target.directory $snapshot)) { $belongs = $false }
            }
            if ($belongs) { $target.estado = 'ACTIVO' }
            else { $target.estado = 'OCUPADO'; $problems += "El puerto configurado para $($target.name) pertenece a otro proceso." }
        }
        if ($target.estado -eq 'PENDIENTE' -and @($knownRecords | Where-Object nombre -eq $target.name).Count) {
            $target.estado = 'EN_INICIO'
            $problems += "Ya existe un proceso de $($target.name) sin puerto disponible. Espera o usa detener.bat antes de reintentar."
        }
    }
    $pending = @($targets | Where-Object estado -eq 'PENDIENTE')
    if (@($pending | Where-Object { $_.kind -ne 'node' -and $_.name -ne 'Gateway' }).Count -and
        (!$env:SERVICES_INTERNAL_KEY -or $env:SERVICES_INTERNAL_KEY.Length -lt 32)) {
        $problems += 'Configura SERVICES_INTERNAL_KEY con al menos 32 caracteres.'
    }
    if (@($pending | Where-Object name -eq 'Autenticacion').Count -and !$env:JWT_KEY) {
        $problems += 'Falta configurar JWT_KEY para Autenticacion.'
    }
    $duplicates = @($targets | Where-Object local | Group-Object { $_.origin.Port } | Where-Object Count -gt 1)
    if ($duplicates.Count) { $problems += 'Hay puertos repetidos entre los servicios locales del .env.' }
    foreach ($kind in @($pending | Select-Object -ExpandProperty kind -Unique)) {
        $command = @{ java = 'java'; dotnet = 'dotnet'; node = 'node' }[$kind]
        if (!(Get-Command $command -ErrorAction SilentlyContinue)) { $problems += "Falta el comando $command." }
    }
    $pendingJava = @($pending | Where-Object kind -eq 'java')
    if ($pendingJava.Count -and !(Get-Command python -ErrorAction SilentlyContinue)) { $problems += 'Falta Python para validar los artefactos Java.' }
    if ($pendingJava.Count -and !$SinCompilar -and !(Get-Command javac -ErrorAction SilentlyContinue)) { $problems += 'Falta el JDK para compilar Java.' }
    foreach ($target in @($pending | Where-Object { $_.name -ne 'Gateway' -and $_.kind -ne 'node' })) {
        if ($ModuloLocal -and $target.name -eq 'Modulo') { continue }
        foreach ($key in @('DB_HOST', 'DB_PORT', 'SERVICES_INTERNAL_KEY')) {
            if (![Environment]::GetEnvironmentVariable($key)) { $problems += "Falta configurar $key." }
        }
        $user = [Environment]::GetEnvironmentVariable('SERVICE_DB_USER_' + $target.name.ToUpperInvariant())
        $password = [Environment]::GetEnvironmentVariable('SERVICE_DB_PASSWORD_' + $target.name.ToUpperInvariant())
        if (!$user) { $user = $env:DB_USERNAME }
        if (!$password) { $password = $env:DB_PASSWORD }
        if (!$user -or !$password) { $problems += "Faltan credenciales SQL de $($target.name)." }
    }
    if ($ModuloLocal) {
        $sql = Get-Service 'MSSQL$SQLEXPRESS' -ErrorAction SilentlyContinue
        if (!$sql -or $sql.Status -ne 'Running') { $problems += 'El modo ModuloLocal requiere SQL Express ya iniciado.' }
    }
    if ($SinCompilar) {
        foreach ($target in $pending) {
            if ($target.kind -eq 'java') {
                $jar = @(Get-ChildItem (Join-Path $target.directory 'target') -Filter '*.jar' -ErrorAction SilentlyContinue)
                if ($jar.Count -ne 1) { $problems += "Falta un JAR unico de $($target.name)." }
            } elseif (!(Test-Path -LiteralPath $target.artifact)) { $problems += "Falta el artefacto de $($target.name)." }
        }
    }
    $targets | Select-Object name, @{n='puerto';e={$_.origin.Port}}, estado | Format-Table -AutoSize
    if ($problems.Count) { foreach ($problem in @($problems | Select-Object -Unique)) { Write-Host "[ERROR] $problem" -ForegroundColor Red }; exit 1 }
    if ($Verificar) { Write-Host 'Configuracion verificada. No se compilo, inicio ni detuvo ningun proceso.'; exit 0 }

    if (!$SinCompilar) {
        if ($pendingJava.Count) {
            $modules = ($pendingJava | ForEach-Object { 'NuevoAvatar.' + $_.name }) -join ','
            & (Join-Path $serviceRoot 'NuevoAvatar.Usuario\mvnw.cmd') -q -f (Join-Path $serviceRoot 'pom.xml') -pl $modules -am package
            if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion Java. No se iniciaron procesos nuevos.' }
        }
        foreach ($target in @($pending | Where-Object kind -eq 'dotnet')) {
            & dotnet build $target.project.FullName --nologo -v:q
            if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion de $($target.name). No se iniciaron procesos nuevos." }
        }
    }
    foreach ($target in $pendingJava) {
        & python (Join-Path $PSScriptRoot 'validar-artefactos.py') --servicio $target.name
        if ($LASTEXITCODE -ne 0) { throw "Artefacto Java invalido: $($target.name)." }
        $target.artifact = (Get-ChildItem (Join-Path $target.directory 'target') -Filter '*.jar' | Select-Object -First 1).FullName
    }
    $records = @(Obtener-RegistroProcesos | Where-Object { Proceso-Coincide $_ })
    New-Item -ItemType Directory -Path $serviceRuntime -Force | Out-Null
    $envNames = @('SERVICE_DB_NAME', 'SERVER_PORT', 'DB_HOST', 'DB_USERNAME', 'DB_PASSWORD', 'SERVICE_DB_USER_MODULO', 'SERVICE_DB_PASSWORD_MODULO', 'ConnectionStrings__SeguridadDb', 'NUEVOAVATAR_HTTPS_PROFILE')
    foreach ($target in $pending) {
        $savedEnv = @{}
        foreach ($key in $envNames) { $savedEnv[$key] = [Environment]::GetEnvironmentVariable($key, 'Process') }
        try {
            $env:SERVICE_DB_NAME = $target.database
            $env:SERVER_PORT = [string]$target.origin.Port
            $env:NUEVOAVATAR_HTTPS_PROFILE = 'false'
            if ($ModuloLocal -and $target.name -eq 'Modulo') {
                $env:DB_HOST = ' '; $env:DB_USERNAME = ' '; $env:DB_PASSWORD = ' '
                $env:SERVICE_DB_USER_MODULO = ' '; $env:SERVICE_DB_PASSWORD_MODULO = ' '
                $env:ConnectionStrings__SeguridadDb = 'Server=.\SQLEXPRESS;Database=PrograV_Modulo;Trusted_Connection=True;TrustServerCertificate=True'
            }
            $executable = @{ java = 'java'; dotnet = 'dotnet'; node = 'node' }[$target.kind]
            $arguments = switch ($target.kind) {
                'java' { @('-Xms32m', '-Xmx192m', '-jar', $target.artifact) }
                default { @($target.artifact) }
            }
            # Start-Process une ArgumentList: conservar comillas para rutas con espacios.
            $quoted = @($arguments | ForEach-Object { '"' + $_ + '"' }) -join ' '
            $child = Start-Process $executable -WindowStyle Hidden -WorkingDirectory $target.directory -ArgumentList $quoted -PassThru `
                -RedirectStandardOutput (Join-Path $serviceRuntime ($target.name + '.out.log')) `
                -RedirectStandardError (Join-Path $serviceRuntime ($target.name + '.err.log'))
            $record = [pscustomobject]@{ nombre = $target.name; pid = $child.Id; inicio = $child.StartTime.ToUniversalTime().Ticks.ToString() }
            $target.proceso = $record
            $records += $record
            Guardar-RegistroProcesos $records
        } catch { $target.estado = 'FALLO'; Write-Host "[ERROR] No se pudo iniciar $($target.name). Revisa outputs\ejecucion." -ForegroundColor Red }
        finally { foreach ($key in $envNames) { [Environment]::SetEnvironmentVariable($key, $savedEnv[$key], 'Process') } }
    }
    $deadline = (Get-Date).AddSeconds($TiempoEspera)
    do {
        $waiting = @()
        $currentSnapshot = Obtener-SnapshotProcesos
        $currentListeners = @(Get-NetTCPConnection -State Listen -ErrorAction Stop)
        foreach ($target in @($pending | Where-Object estado -eq 'PENDIENTE')) {
            if (!$target.proceso -or !(Proceso-Coincide $target.proceso)) { $target.estado = 'FALLO'; continue }
            $owner = @($currentListeners | Where-Object LocalPort -eq $target.origin.Port | Select-Object -ExpandProperty OwningProcess -Unique)
            if ($owner.Count -and @($owner | Where-Object { Pertenece-Directorio $currentSnapshot[[int]$_] $target.directory $currentSnapshot }).Count -eq $owner.Count) { $target.estado = 'ACTIVO' }
            else { $waiting += $target }
        }
        if (!$waiting.Count) { break }
        Start-Sleep -Milliseconds 500
    } while ((Get-Date) -lt $deadline)
    foreach ($target in @($targets | Where-Object estado -eq 'PENDIENTE')) { $target.estado = 'SIN_RESPUESTA' }
    $targets | Select-Object name, @{n='puerto';e={$_.origin.Port}}, estado | Format-Table -AutoSize
    if (@($targets | Where-Object { $_.estado -in @('FALLO', 'SIN_RESPUESTA') }).Count) { Write-Host 'Hay servicios pendientes. Revisa outputs\ejecucion y usa detener.bat para cerrar los procesos iniciados.'; exit 1 }
    Write-Host 'Servicios disponibles en sus puertos configurados. Frontend: consulta FRONTEND_BASE_URL en el .env.'
    exit 0
} catch { Write-Host ("[ERROR] " + $_.Exception.Message) -ForegroundColor Red; exit 1 }
finally { if ($operationLock) { $operationLock.ReleaseMutex(); $operationLock.Dispose() } }
