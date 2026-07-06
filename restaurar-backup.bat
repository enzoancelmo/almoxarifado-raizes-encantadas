@echo off
setlocal
cd /d "%~dp0"
set "ARQUIVO=%~1"
if "%ARQUIVO%"=="" (
  echo Arraste um arquivo .sql sobre este script ou informe o caminho abaixo.
  set /p "ARQUIVO=Caminho do backup: "
)
if not exist "%ARQUIVO%" (
  echo Arquivo nao encontrado: %ARQUIVO%
  pause
  exit /b 1
)
echo.
echo ATENCAO: a restauracao altera os dados atuais.
choice /c SN /m "Deseja continuar"
if errorlevel 2 exit /b 0
type "%ARQUIVO%" | docker compose exec -T postgres sh -c "psql -v ON_ERROR_STOP=1 -U ""$POSTGRES_USER"" -d ""$POSTGRES_DB"""
if errorlevel 1 (
  echo Falha na restauracao. Consulte os logs do Docker.
  pause
  exit /b 1
)
echo Restauracao concluida.
pause
