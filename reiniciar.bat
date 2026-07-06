@echo off
setlocal
cd /d "%~dp0"
docker compose down
if errorlevel 1 exit /b 1
docker compose up -d --build
if errorlevel 1 (
  echo Falha ao reiniciar. Verifique o Docker Desktop.
  pause
  exit /b 1
)
echo Sistema reiniciado em http://localhost
pause
