@echo off
setlocal
start "NuevoAvatar.Curso - 5255" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Curso
start "NuevoAvatar.Grupo - 5256" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Grupo
start "NuevoAvatar.Periodo - 5262" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Periodo
start "NuevoAvatar.Direccion - 5227" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Direccion
echo Servicios de Persona 3 iniciados. Conecta Tailscale y configura .env antes de ejecutarlos.
pause
