@echo off
setlocal
title Detener servicios de Nuevo Avatar
rem Detengo solo los procesos del proyecto; no SQL Server ni Tailscale.
powershell.exe -NoProfile -File "%~dp0tools\detener-servicios.ps1"
set "taskExitCode=%errorlevel%"
echo.
pause
exit /b %taskExitCode%
