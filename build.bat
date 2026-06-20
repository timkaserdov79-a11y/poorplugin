@echo off
REM Build script for PoorPlugin (Windows)

echo =========================================
echo Building PoorPlugin...
echo =========================================

REM Проверяем наличие Maven
mvn --version >nul 2>&1
if errorlevel 1 (
    echo Maven not found! Install Maven first.
    exit /b 1
)

REM Очищаем и собираем
mvn clean package

if errorlevel 1 (
    echo.
    echo =========================================
    echo X Build failed!
    echo =========================================
    exit /b 1
)

echo.
echo =========================================
echo + Build successful!
echo + Plugin ready: target\poorplugin-1.0.0.jar
echo =========================================

REM Копируем в папку plugins если она есть
if exist "plugins" (
    copy target\poorplugin-1.0.0.jar plugins\
    echo + Plugin copied to plugins\ folder
)
