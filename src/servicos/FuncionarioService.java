package servicos;

import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import excecoes.ValidacaoException;
import interfaces.IFuncionarioService;
import interfaces.IOcorrenciaRepository;
import java.util.List;

// Service do Funcionario - so pode ver suas ocorrencias e mudar status temporario
public class FuncionarioService implements IFuncionarioService {

    private final IOcorrenciaRepository ocorrenciaRepo;

    public FuncionarioService(IOcorrenciaRepository ocorrenciaRepo) {
        this.ocorrenciaRepo = ocorrenciaRepo;
    }

    @Override
    public List<Ocorrencia> listarMinhasOcorrencias(String matricula) {
        return ocorrenciaRepo.listarPorFuncionario(matricula);
    }

    // R5: so o funcionario alocado pode alterar o status temporario
    @Override
    public void alterarStatusTemporario(int numero, String matricula, StatusOcorrencia novoStatus)
            throws ValidacaoException {
        Ocorrencia oc = ocorrenciaRepo.buscarPorNumero(numero);
        if (oc == null || !oc.getFuncionarioAlocado().getMatricula().equalsIgnoreCase(matricula))
            throw new ValidacaoException("Ocorrencia #" + numero + " nao encontrada ou nao esta atribuida a voce.");
        oc.setStatusTemporario(novoStatus);
        ocorrenciaRepo.atualizar(oc);
    }
}
