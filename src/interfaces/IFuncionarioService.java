package interfaces;

import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import excecoes.ValidacaoException;
import java.util.List;

/**
 * ISP - Interface Segregation Principle:
 * Interface especifica para as operacoes do Funcionario alocado.
 * O Funcionario so pode visualizar suas proprias ocorrencias e
 * alterar o status temporario (R5 - limitacao do funcionario).
 * Nao contem metodos de cadastro, departamentos ou status definitivo.
 */
public interface IFuncionarioService {

    /**
     * R5: Funcionario visualiza apenas ocorrencias atribuidas a ele.
     */
    List<Ocorrencia> listarMinhasOcorrencias(String matricula);

    /**
     * R5 - Limitacao do Funcionario: apenas o funcionario alocado
     * pode alterar o status temporario da ocorrencia.
     */
    void alterarStatusTemporario(int numero, String matricula, StatusOcorrencia novoStatus)
        throws ValidacaoException;
}
