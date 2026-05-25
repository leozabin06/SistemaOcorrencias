package interfaces;

import entidades.Departamento;
import entidades.Funcionario;
import entidades.Gerente;
import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import java.time.LocalDate;
import java.util.List;

/**
 * ISP - Interface Segregation Principle:
 * Interface especifica para as operacoes do Gerente de Departamento.
 * O Gerente gerencia funcionarios e registra/encerra ocorrencias (R5).
 * Nao contem metodos exclusivos do Diretor.
 */
public interface IGerenteServico {

    // --- Funcionarios (R4) ---
    void cadastrarFuncionario(String matricula, String nome, int codigoDepto, String status)
        throws ErroValidacao;

    void alterarFuncionario(String matricula, String nome, int codigoDepto, String status)
        throws ErroValidacao;

    List<Funcionario> listarFuncionarios();

    /**
     * Retorna apenas funcionarios do departamento de Informatica (R6).
     */
    List<Funcionario> listarFuncionariosInformatica();

    List<Departamento> listarDepartamentos();

    // --- Ocorrencias (R5) ---
    void registrarOcorrencia(Gerente gerente, String descricao, LocalDate dataOcorrencia,
                              LocalDate dataLimite, String matriculaFuncAlocado)
        throws ErroValidacao;

    /**
     * Altera campos da ocorrencia. Parametros nulos indicam "sem alteracao".
     */
    void alterarOcorrencia(int numero, Gerente gerente, String novaDescricao,
                           LocalDate novaDataLimite, String novaMatriculaFunc)
        throws ErroValidacao;

    /**
     * R5 - Limitacao do Gerente: apenas o gerente do depto reportante
     * pode alterar o status definitivo.
     */
    void alterarStatusDefinitivo(int numero, Gerente gerente, StatusOcorrencia novoStatus)
        throws ErroValidacao;

    List<Ocorrencia> listarOcorrenciasPorDepto(int codigoDepto);
}
