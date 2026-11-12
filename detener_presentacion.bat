@echo off
setlocal
title Nuevo Avatar - Detener presentacion
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\detener-servicios.ps1"
set "taskExitCode=%errorlevel%"
echo.
pause
exit /b %taskExitCode%
