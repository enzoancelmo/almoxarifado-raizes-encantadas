@echo off
setlocal
for /f "usebackq delims=" %%D in (`powershell -NoProfile -Command "[Environment]::GetFolderPath('Desktop')"`) do set "DESKTOP=%%D"
if not defined DESKTOP (
  echo Nao foi possivel localizar a Area de Trabalho.
  pause
  exit /b 1
)
(
  echo [InternetShortcut]
  echo URL=http://localhost:4200
  echo IconFile=C:\Windows\System32\shell32.dll
  echo IconIndex=220
) > "%DESKTOP%\Estoque Inteligente.url"
echo.
echo Atalho criado com sucesso em:
echo %DESKTOP%\Estoque Inteligente.url
start "" "%DESKTOP%"
timeout /t 3 >nul
