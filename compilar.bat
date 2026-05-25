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
  "%SRC%\entities\Departamento.java" ^
  "%SRC%\entities\Diretor.java" ^
  "%SRC%\entities\Gerente.java" ^
  "%SRC%\entities\Funcionario.java" ^
  "%SRC%\entities\Ocorrencia.java" ^
  "%SRC%\enums\StatusOcorrencia.java" ^
  "%SRC%\exceptions\ValidacaoException.java" ^
  "%SRC%\interfaces\IDepartamentoRepository.java" ^
  "%SRC%\interfaces\IDiretorRepository.java" ^
  "%SRC%\interfaces\IGerenteRepository.java" ^
  "%SRC%\interfaces\IFuncionarioRepository.java" ^
  "%SRC%\interfaces\IOcorrenciaRepository.java" ^
  "%SRC%\interfaces\IDiretorService.java" ^
  "%SRC%\interfaces\IGerenteService.java" ^
  "%SRC%\interfaces\IFuncionarioService.java" ^
  "%SRC%\validators\ValidacaoOcorrencia.java" ^
  "%SRC%\db\ConexaoDB.java" ^
  "%SRC%\repositories\DepartamentoRepository.java" ^
  "%SRC%\repositories\DiretorRepository.java" ^
  "%SRC%\repositories\GerenteRepository.java" ^
  "%SRC%\repositories\FuncionarioRepository.java" ^
  "%SRC%\repositories\OcorrenciaRepository.java" ^
  "%SRC%\repositories\mysql\DepartamentoRepositoryMySQL.java" ^
  "%SRC%\repositories\mysql\DiretorRepositoryMySQL.java" ^
  "%SRC%\repositories\mysql\GerenteRepositoryMySQL.java" ^
  "%SRC%\repositories\mysql\FuncionarioRepositoryMySQL.java" ^
  "%SRC%\repositories\mysql\OcorrenciaRepositoryMySQL.java" ^
  "%SRC%\services\DiretorService.java" ^
  "%SRC%\services\GerenteService.java" ^
  "%SRC%\services\FuncionarioService.java" ^
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
