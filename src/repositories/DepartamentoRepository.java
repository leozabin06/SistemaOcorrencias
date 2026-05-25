package repositories;

import entities.Departamento;
import interfaces.IDepartamentoRepository;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoRepository implements IDepartamentoRepository {

    private final List<Departamento> dados = new ArrayList<>();

    @Override
    public void salvar(Departamento departamento) {
        dados.add(departamento);
    }

    @Override
    public Departamento buscarPorCodigo(int codigo) {
        return dados.stream()
            .filter(d -> d.getCodigo() == codigo)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Departamento> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public boolean existeCodigo(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    @Override
    public void atualizar(Departamento departamento) {
        // em memoria: o objeto ja foi modificado via setters, nada a fazer
    }
}
