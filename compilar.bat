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
  "%SRC%\excecoes\ValidacaoException.java" ^
  "%SRC%\interfaces\IDepartamentoRepository.java" ^
  "%SRC%\interfaces\IDiretorRepository.java" ^
  "%SRC%\interfaces\IGerenteRepository.java" ^
  "%SRC%\interfaces\IFuncionarioRepository.java" ^
  "%SRC%\interfaces\IOcorrenciaRepository.java" ^
  "%SRC%\interfaces\IDiretorService.java" ^
  "%SRC%\interfaces\IGerenteService.java" ^
  "%SRC%\interfaces\IFuncionarioService.java" ^
  "%SRC%\validadores\ValidacaoOcorrencia.java" ^
  "%SRC%\db\ConexaoDB.java" ^
  "%SRC%\repositorios\DepartamentoRepository.java" ^
  "%SRC%\repositorios\DiretorRepository.java" ^
  "%SRC%\repositorios\GerenteRepository.java" ^
  "%SRC%\repositorios\FuncionarioRepository.java" ^
  "%SRC%\repositorios\OcorrenciaRepository.java" ^
  "%SRC%\repositorios\mysql\DepartamentoRepositoryMySQL.java" ^
  "%SRC%\repositorios\mysql\DiretorRepositoryMySQL.java" ^
  "%SRC%\repositorios\mysql\GerenteRepositoryMySQL.java" ^
  "%SRC%\repositorios\mysql\FuncionarioRepositoryMySQL.java" ^
  "%SRC%\repositorios\mysql\OcorrenciaRepositoryMySQL.java" ^
  "%SRC%\servicos\DiretorService.java" ^
  "%SRC%\servicos\GerenteService.java" ^
  "%SRC%\servicos\FuncionarioService.java" ^
  "%SRC%\ui\swing\MainFrame.java" ^
  "%SRC%\ui\swing\LoginPanel.java" ^
  "%SRC%\ui\swing\DiretorPanel.java" ^
  "%SRC%\ui\swing\GerentePanel.java" ^
  "%SRC%\ui\swing\FuncionarioPanel.java" ^
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
