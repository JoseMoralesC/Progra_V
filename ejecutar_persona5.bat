@echo off
setlocal
rem Inicio cada servicio independiente de mi persona en su propia consola.
start "NuevoAvatar.Matricula - 8085" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Matricula
start "NuevoAvatar.Notas - 8092" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Notas
start "NuevoAvatar.Factura - 8093" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Factura
start "NuevoAvatar.Pago - 8094" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Pago
start "NuevoAvatar.Notificacion - 8095" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Notificacion
echo Servicios de Persona 5 iniciados. Conecta Tailscale y configura .env antes de ejecutarlos.
pause
