package interfaces;

import entidades.Departamento;
import entidades.Gerente;
import erros.ErroValidacao;
import java.util.List;

/**
 * ISP - Interface Segregation Principle:
 * Interface especifica para as operacoes do Diretor.
 * O Diretor gerencia departamentos e gerentes.
 * Nao contem metodos de funcionarios ou ocorrencias.
 */
public interface IDiretorServico {

    // --- Departamentos (R1) ---
    void cadastrarDepartamento(int codigo, String nome, String descricao, String status)
        throws ErroValidacao;

    void alterarDepartamento(int codigo, String nome, String descricao, String status)
        throws ErroValidacao;

    List<Departamento> listarDepartamentos();

    Departamento buscarDepartamento(int codigo);

    // --- Gerentes (R3) ---
    void cadastrarGerente(String matricula, String nome, int codigoDepto, String status)
        throws ErroValidacao;

    void alterarGerente(String matricula, String nome, int codigoDepto, String status)
        throws ErroValidacao;

    List<Gerente> listarGerentes();

    Gerente buscarGerente(String matricula);
}
