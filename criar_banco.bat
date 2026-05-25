@echo off
chcp 65001 >nul
echo ============================================
echo   Criando Banco de Dados no MySQL
echo ============================================
echo.
set /p SENHA="Digite sua senha do MySQL root: "

set BASE=%~dp0
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p%SENHA% < "%BASE%database\schema.sql"

if %ERRORLEVEL% == 0 (
    echo.
    echo [OK] Banco 'ocorrencias_ti' criado com sucesso!
    echo.
    echo IMPORTANTE: Abra src\db\ConexaoDB.java
    echo e coloque sua senha em: PASSWORD = "sua_senha"
) else (
    echo.
    echo [ERRO] Falha ao criar banco. Verifique a senha.
)
pause
