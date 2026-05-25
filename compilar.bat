@echo off
chcp 65001 >nul
echo ============================================
echo   Compilando Sistema de Ocorrencias - TI
echo ============================================

set BASE=%~dp0
set SRC=%BASE%src
set BIN=%BASE%bin
set LIB=%BASE%lib\mysql-connector-j-8.0.33.jar

if exist "%BIN%" rmdir /s /q "%BIN%"
mkdir "%BIN%"

javac -encoding UTF-8 -cp "%LIB%" -d "%BIN%" ^
  "%SRC%\entidades\Departamento.java" ^
  "%SRC%\entidades\Diretor.java" ^
  "%SRC%\entidades\Gerente.java" ^
  "%SRC%\entidades\Funcionario.java" ^
  "%SRC%\entidades\Ocorrencia.java" ^
  "%SRC%\enums\StatusOcorrencia.java" ^
  "%SRC%\erros\ErroValidacao.java" ^
  "%SRC%\interfaces\IDepartamentoRepositorio.java" ^
  "%SRC%\interfaces\IDiretorRepositorio.java" ^
  "%SRC%\interfaces\IGerenteRepositorio.java" ^
  "%SRC%\interfaces\IFuncionarioRepositorio.java" ^
  "%SRC%\interfaces\IOcorrenciaRepositorio.java" ^
  "%SRC%\interfaces\IDiretorServico.java" ^
  "%SRC%\interfaces\IGerenteServico.java" ^
  "%SRC%\interfaces\IFuncionarioServico.java" ^
  "%SRC%\validadores\ValidacaoOcorrencia.java" ^
  "%SRC%\db\ConexaoDB.java" ^
  "%SRC%\repositorios\DepartamentoRepositorio.java" ^
  "%SRC%\repositorios\DiretorRepositorio.java" ^
  "%SRC%\repositorios\GerenteRepositorio.java" ^
  "%SRC%\repositorios\FuncionarioRepositorio.java" ^
  "%SRC%\repositorios\OcorrenciaRepositorio.java" ^
  "%SRC%\repositorios\mysql\DepartamentoRepositorioMySQL.java" ^
  "%SRC%\repositorios\mysql\DiretorRepositorioMySQL.java" ^
  "%SRC%\repositorios\mysql\GerenteRepositorioMySQL.java" ^
  "%SRC%\repositorios\mysql\FuncionarioRepositorioMySQL.java" ^
  "%SRC%\repositorios\mysql\OcorrenciaRepositorioMySQL.java" ^
  "%SRC%\servicos\DiretorServico.java" ^
  "%SRC%\servicos\GerenteServico.java" ^
  "%SRC%\servicos\FuncionarioServico.java" ^
  "%SRC%\ui\swing\TelaPrincipal.java" ^
  "%SRC%\ui\swing\PainelLogin.java" ^
  "%SRC%\ui\swing\PainelDiretor.java" ^
  "%SRC%\ui\swing\PainelGerente.java" ^
  "%SRC%\ui\swing\PainelFuncionario.java" ^
  "%SRC%\App.java"

if %ERRORLEVEL% == 0 (
    echo.
    echo [OK] Compilacao concluida com sucesso!
    echo Para executar, rode executar.bat
) else (
    echo.
    echo [ERRO] Falha na compilacao.
)
pause
