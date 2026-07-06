@echo off
setlocal
cd /d "%~dp0"
if not exist ".env" (
  echo ERRO: arquivo .env nao encontrado.
  echo Copie .env.example para .env e altere as senhas antes de iniciar.
  pause
  exit /b 1
)
echo Iniciando o Estoque Inteligente...
docker compose up -d --build
if errorlevel 1 (
  echo.
  echo Nao foi possivel iniciar. Verifique se o Docker Desktop esta aberto.
  pause
  exit /b 1
)
echo.
echo Sistema iniciado em http://localhost
docker compose ps
pause
