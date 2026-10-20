@echo off
setlocal
rem Inicio cada servicio independiente de mi persona en su propia consola.
start "NuevoAvatar.Autenticacion - 5158" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Autenticacion
start "NuevoAvatar.Bitacora - 5159" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Bitacora
start "NuevoAvatar.Rol - 5160" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Rol
start "NuevoAvatar.Parametro - 5161" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Parametro
start "NuevoAvatar.Modulo - 5162" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Modulo
echo Servicios de Persona 1 iniciados. Conecta Tailscale y configura .env antes de ejecutarlos.
pause
