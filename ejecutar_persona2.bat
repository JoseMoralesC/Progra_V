@echo off
setlocal
rem Inicio cada servicio independiente de mi persona en su propia consola.
start "NuevoAvatar.Usuario - 8082" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Usuario
start "NuevoAvatar.Institucion - 8086" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Institucion
start "NuevoAvatar.Profesor - 8087" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Profesor
start "NuevoAvatar.Carrera - 8088" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Carrera
echo Servicios de Persona 2 iniciados. Conecta Tailscale y configura .env antes de ejecutarlos.
pause
