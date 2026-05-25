package interfaces;

import entities.Departamento;
import entities.Gerente;
import exceptions.ValidacaoException;
import java.util.List;

/**
 * ISP - Interface Segregation Principle:
 * Interface especifica para as operacoes do Diretor.
 * O Diretor gerencia departamentos e gerentes.
 * Nao contem metodos de funcionarios ou ocorrencias.
 */
public interface IDiretorService {

    // --- Departamentos (R1) ---
    void cadastrarDepartamento(int codigo, String nome, String descricao, String status)
        throws ValidacaoException;

    void alterarDepartamento(int codigo, String nome, String descricao, String status)
        throws ValidacaoException;

    List<Departamento> listarDepartamentos();

    Departamento buscarDepartamento(int codigo);

    // --- Gerentes (R3) ---
    void cadastrarGerente(String matricula, String nome, int codigoDepto, String status)
        throws ValidacaoException;

    void alterarGerente(String matricula, String nome, int codigoDepto, String status)
        throws ValidacaoException;

    List<Gerente> listarGerentes();

    Gerente buscarGerente(String matricula);
}
