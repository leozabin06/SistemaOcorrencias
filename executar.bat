@echo off
chcp 65001 >nul
echo ============================================
echo   Executando Sistema de Ocorrencias - TI
echo ============================================

set BASE=%~dp0
set BIN=%BASE%bin
set LIB=%BASE%lib\mysql-connector-j-8.0.33.jar

java -cp "%BIN%;%LIB%" App
pause
