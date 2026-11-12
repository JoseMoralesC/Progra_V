@echo off
setlocal
title Nuevo Avatar - Presentacion
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\ejecutar-presentacion.ps1" %*
set "taskExitCode=%errorlevel%"
echo.
if not "%taskExitCode%"=="0" echo La presentacion no quedo completamente iniciada.
pause
exit /b %taskExitCode%
