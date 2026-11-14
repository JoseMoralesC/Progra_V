@echo off
setlocal
title Nuevo Avatar - Iniciar
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\iniciar.ps1" %*
set "taskExitCode=%errorlevel%"
if "%~1"=="" pause
exit /b %taskExitCode%
