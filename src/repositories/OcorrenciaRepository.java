package repositories;

import entities.Ocorrencia;
import interfaces.IOcorrenciaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OcorrenciaRepository implements IOcorrenciaRepository {

    private final List<Ocorrencia> dados = new ArrayList<>();

    @Override
    public void salvar(Ocorrencia ocorrencia) {
        dados.add(ocorrencia);
    }

    @Override
    public Ocorrencia buscarPorNumero(int numero) {
        return dados.stream()
            .filter(o -> o.getNumero() == numero)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Ocorrencia> listarPorDepartamento(int codigoDepto) {
        return dados.stream()
            .filter(o -> o.getDeptoReportante().getCodigo() == codigoDepto)
            .collect(Collectors.toList());
    }

    @Override
    public List<Ocorrencia> listarPorFuncionario(String matricula) {
        return dados.stream()
            .filter(o -> o.getFuncionarioAlocado().getMatricula().equalsIgnoreCase(matricula))
            .collect(Collectors.toList());
    }

    @Override
    public int proximoNumero() {
        return dados.size() + 1;
    }

    @Override
    public void atualizar(Ocorrencia ocorrencia) {
        // em memoria: o objeto ja foi modificado via setters, nada a fazer
    }
}
