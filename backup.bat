@echo off
setlocal
cd /d "%~dp0"
if not exist "backups" mkdir "backups"
for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyy-MM-dd_HH-mm-ss"') do set "STAMP=%%i"
set "ARQUIVO=backups\backup_raizes_%STAMP%.sql"
echo Criando backup em %ARQUIVO%...
docker compose exec -T postgres sh -c "pg_dump -U ""$POSTGRES_USER"" -d ""$POSTGRES_DB""" > "%ARQUIVO%"
if errorlevel 1 (
  del "%ARQUIVO%" 2>nul
  echo Falha no backup. Confirme se o sistema esta iniciado.
  pause
  exit /b 1
)
echo Backup concluido: %ARQUIVO%
pause
