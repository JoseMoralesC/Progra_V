param([switch]$SoloRamses, [switch]$SinFrontend, [switch]$ModuloLocal, [switch]$SinCompilar)
$ErrorActionPreference = 'Stop'
$raiz = [IO.Path]::GetFullPath((Split-Path $PSScriptRoot -Parent))
$salida = Join-Path $raiz 'outputs\presentacion'
New-Item -ItemType Directory -Force -Path $salida | Out-Null
$manifiesto = Get-Content (Join-Path $PSScriptRoot 'servicios.json') -Raw | ConvertFrom-Json

function Probar-Puerto([int]$Puerto) {
    return [bool](Get-NetTCPConnection -LocalPort $Puerto -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1)
}

Write-Host '=== NUEVO AVATAR: PREPARACION DE PRESENTACION ===' -ForegroundColor Cyan
if (!(Test-Path (Join-Path $raiz '.env'))) { throw 'Falta el .env local.' }
if (!(Get-Command tailscale -ErrorAction SilentlyContinue)) { throw 'Tailscale no esta instalado.' }
$estadoTailscale = (& tailscale status 2>&1 | Out-String)
if ($LASTEXITCODE -ne 0 -or $estadoTailscale -notmatch '100\.124\.109\.126.+active') { throw 'El equipo de Jose no aparece activo en Tailscale.' }
Write-Host '[OK] Tailscale conectado con Jose.' -ForegroundColor Green

if ($ModuloLocal) {
    $sql = Get-Service 'MSSQL$SQLEXPRESS' -ErrorAction Stop
    if ($sql.Status -ne 'Running') { Start-Service 'MSSQL$SQLEXPRESS'; $sql.WaitForStatus('Running',[TimeSpan]::FromSeconds(20)) }
    $consultaLocal = "SET NOCOUNT ON; SELECT CASE WHEN OBJECT_ID('PrograV_Modulo.seguridad.rol_modulo','U') IS NOT NULL THEN 1 ELSE 0 END"
    $verificacion = & sqlcmd -S '.\SQLEXPRESS' -E -C -h -1 -W -Q $consultaLocal 2>&1
    if ($LASTEXITCODE -ne 0 -or ($verificacion | Out-String).Trim() -ne '1') { throw 'Falta la estructura local de Modulo.' }
    Write-Host '[OK] Modulo local verificado; Notificacion usara Tailscale.' -ForegroundColor Green
} else {
    Write-Host '[OK] Modulo y Notificacion usaran Tailscale.' -ForegroundColor Green
}

if ($SoloRamses) {
    $nombres = @('Bitacora','Rol','Parametro','Usuario','Autenticacion','Modulo','Institucion','Profesor','Carrera','Curso','Grupo','Periodo','Notificacion')
    $servicios = @($manifiesto | Where-Object name -in $nombres)
} else { $servicios = @($manifiesto) }

if (!$SinCompilar) {
    Write-Host '[INFO] Verificando y compilando artefactos en serie...' -ForegroundColor Cyan
    foreach ($servicio in $servicios) {
        if (Probar-Puerto $servicio.port) {
            Write-Host "[OMITIDO] $($servicio.name): ya esta activo." -ForegroundColor DarkGray
            continue
        }
        if ($servicio.kind -eq 'java') {
            $directorio = Join-Path $raiz ('NuevoAvatar.' + $servicio.name)
            $jar = Get-ChildItem (Join-Path $directorio 'target') -Filter '*.jar' -ErrorAction SilentlyContinue | Select-Object -First 1
            $fuente = Get-ChildItem $directorio -Recurse -File -Include '*.java','pom.xml' |
                Sort-Object LastWriteTime -Descending | Select-Object -First 1
            if (!$jar -or ($fuente -and $fuente.LastWriteTime -gt $jar.LastWriteTime)) {
                & (Join-Path $PSScriptRoot 'compilar-servicios.ps1') -Servicio $servicio.name
                if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion de $($servicio.name)." }
            } else {
                Write-Host "[OK] $($servicio.name): JAR vigente." -ForegroundColor Green
            }
        } else {
            $proyecto = Get-ChildItem (Join-Path $raiz ('NuevoAvatar.' + $servicio.name)) -Filter '*.csproj' | Select-Object -First 1
            & dotnet build $proyecto.FullName --no-restore --nologo -v:q
            if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion de $($servicio.name)." }
        }
    }
    if (!(Probar-Puerto 5080)) {
        & dotnet build (Join-Path $raiz 'NuevoAvatar.Gateway\NuevoAvatar.Gateway.csproj') --no-restore --nologo -v:q
        if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion de Gateway.' }
    }
    Write-Host '[OK] Compilacion serial completada.' -ForegroundColor Green
} else {
    Write-Host '[AVISO] Se omitio la compilacion por solicitud.' -ForegroundColor Yellow
}

$iniciados = @(); $existentes = @(); $fallidos = @()
foreach ($servicio in $servicios) {
    if (Probar-Puerto $servicio.port) { $existentes += $servicio; continue }
    $out = Join-Path $salida ($servicio.name + '.out.log')
    $err = Join-Path $salida ($servicio.name + '.err.log')
    $argumentos = @('-NoProfile','-ExecutionPolicy','Bypass','-File',(Join-Path $PSScriptRoot 'ejecutar-presentacion-servicio.ps1'),'-Servicio',$servicio.name)
    if ($servicio.name -eq 'Modulo' -and $ModuloLocal) { $argumentos += '-ModuloLocal' }
    Start-Process powershell.exe -WindowStyle Hidden -WorkingDirectory $raiz -ArgumentList $argumentos -RedirectStandardOutput $out -RedirectStandardError $err
    $iniciados += $servicio
}

# Los servicios arrancan en paralelo; espero hasta dos minutos sin bloquear indefinidamente.
$limite = (Get-Date).AddMinutes(2)
do {
    $pendientes = @($iniciados | Where-Object { !(Probar-Puerto $_.port) })
    if (!$pendientes.Count) { break }
    Start-Sleep -Seconds 2
} while ((Get-Date) -lt $limite)
$fallidos = @($iniciados | Where-Object { !(Probar-Puerto $_.port) })

if (!(Probar-Puerto 5080)) {
    Start-Process dotnet -WindowStyle Hidden -WorkingDirectory (Join-Path $raiz 'NuevoAvatar.Gateway') -ArgumentList @(
        'run','--no-build','--no-restore','--project',(Join-Path $raiz 'NuevoAvatar.Gateway\NuevoAvatar.Gateway.csproj'),'--launch-profile','http'
    ) -RedirectStandardOutput (Join-Path $salida 'Gateway.out.log') -RedirectStandardError (Join-Path $salida 'Gateway.err.log')
    $tope=(Get-Date).AddSeconds(30); while(!(Probar-Puerto 5080) -and (Get-Date)-lt $tope){Start-Sleep -Milliseconds 500}
}

if (!$SinFrontend -and !(Probar-Puerto 4175)) {
    $web = Join-Path $raiz 'NuevoAvatar.Estudiante.React'
    if (!(Test-Path (Join-Path $web 'node_modules'))) { throw 'Ejecuta npm ci en NuevoAvatar.Estudiante.React.' }
    Start-Process cmd.exe -WindowStyle Hidden -WorkingDirectory $web -ArgumentList @('/c','npm run dev') -RedirectStandardOutput (Join-Path $salida 'Frontend.out.log') -RedirectStandardError (Join-Path $salida 'Frontend.err.log')
    $tope=(Get-Date).AddSeconds(30); while(!(Probar-Puerto 4175) -and (Get-Date)-lt $tope){Start-Sleep -Milliseconds 500}
}

Write-Host "`n=== RESUMEN ===" -ForegroundColor Cyan
foreach($s in $servicios){Write-Host ("{0,-22} {1,5}  {2}" -f $s.name,$s.port,$(if(Probar-Puerto $s.port){'ACTIVO'}else{'FALLO'})) -ForegroundColor $(if(Probar-Puerto $s.port){'Green'}else{'Red'})}
Write-Host ("{0,-22} {1,5}  {2}" -f 'Gateway',5080,$(if(Probar-Puerto 5080){'ACTIVO'}else{'FALLO'}))
if(!$SinFrontend){Write-Host ("{0,-22} {1,5}  {2}" -f 'Frontend',4175,$(if(Probar-Puerto 4175){'ACTIVO'}else{'FALLO'}))}
if ($fallidos.Count -or !(Probar-Puerto 5080) -or (!$SinFrontend -and !(Probar-Puerto 4175))) {
    Write-Host "Revisa los logs en $salida" -ForegroundColor Yellow
    exit 1
}
Write-Host 'Presentacion lista: http://127.0.0.1:4175' -ForegroundColor Green
exit 0
