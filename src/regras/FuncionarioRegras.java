package regras;

import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import interfaces.IFuncionarioRegras;
import interfaces.IOcorrenciaRepositorio;
import java.util.List;

// Service do Funcionario - so pode ver suas ocorrencias e mudar status temporario
public class FuncionarioRegras implements IFuncionarioRegras {

    private final IOcorrenciaRepositorio ocorrenciaRepo;

    public FuncionarioRegras(IOcorrenciaRepositorio ocorrenciaRepo) {
        this.ocorrenciaRepo = ocorrenciaRepo;
    }

    @Override
    public List<Ocorrencia> listarMinhasOcorrencias(String matricula) {
        return ocorrenciaRepo.listarPorFuncionario(matricula);
    }

    // R5: so o funcionario alocado pode alterar o status temporario
    @Override
    public void alterarStatusTemporario(int numero, String matricula, StatusOcorrencia novoStatus)
            throws ErroValidacao {
        Ocorrencia oc = ocorrenciaRepo.buscarPorNumero(numero);
        if (oc == null || !oc.getFuncionarioAlocado().getMatricula().equalsIgnoreCase(matricula))
            throw new ErroValidacao("Ocorrencia #" + numero + " nao encontrada ou nao esta atribuida a voce.");
        oc.setStatusTemporario(novoStatus);
        ocorrenciaRepo.atualizar(oc);
    }
}
