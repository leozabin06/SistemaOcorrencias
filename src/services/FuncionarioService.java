package services;

import entities.Ocorrencia;
import enums.StatusOcorrencia;
import exceptions.ValidacaoException;
import interfaces.IFuncionarioService;
import interfaces.IOcorrenciaRepository;
import java.util.List;

/**
 * IoC - Inversao de Controle:
 * FuncionarioService recebe IOcorrenciaRepository via construtor.
 * Nao conhece a implementacao concreta do repositorio.
 */
public class FuncionarioService implements IFuncionarioService {

    private final IOcorrenciaRepository ocorrenciaRepo;

    public FuncionarioService(IOcorrenciaRepository ocorrenciaRepo) {
        this.ocorrenciaRepo = ocorrenciaRepo;
    }

    @Override
    public List<Ocorrencia> listarMinhasOcorrencias(String matricula) {
        return ocorrenciaRepo.listarPorFuncionario(matricula);
    }

    /**
     * R5 - Limitacao do Funcionario:
     * Somente o funcionario alocado pode alterar o status temporario.
     */
    @Override
    public void alterarStatusTemporario(int numero, String matricula, StatusOcorrencia novoStatus)
            throws ValidacaoException {
        Ocorrencia oc = ocorrenciaRepo.buscarPorNumero(numero);
        if (oc == null || !oc.getFuncionarioAlocado().getMatricula().equalsIgnoreCase(matricula)) {
            throw new ValidacaoException(
                "Ocorrencia #" + numero + " nao encontrada ou nao esta atribuida a voce.");
        }
        oc.setStatusTemporario(novoStatus);
        ocorrenciaRepo.atualizar(oc);
    }
}
