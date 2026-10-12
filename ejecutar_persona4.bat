@echo off
setlocal
rem Inicio cada servicio independiente de mi persona en su propia consola.
start "NuevoAvatar.Expediente - 8084" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Expediente
start "NuevoAvatar.Prematricula - 8089" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio Prematricula
start "NuevoAvatar.HistorialAcademico - 8090" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio HistorialAcademico
start "NuevoAvatar.ListadoEstudiantes - 8091" "%ComSpec%" /k powershell.exe -NoProfile -File "%~dp0tools\ejecutar-servicio.ps1" -Servicio ListadoEstudiantes
echo Servicios de Persona 4 iniciados. Conecta Tailscale y configura .env antes de ejecutarlos.
pause
