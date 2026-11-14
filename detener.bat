@echo off
setlocal
title Nuevo Avatar - Detener
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\detener.ps1" %*
set "taskExitCode=%errorlevel%"
if "%~1"=="" pause
exit /b %taskExitCode%
