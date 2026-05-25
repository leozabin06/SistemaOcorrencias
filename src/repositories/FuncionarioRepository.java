package repositories;

import entities.Funcionario;
import interfaces.IFuncionarioRepository;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository implements IFuncionarioRepository {

    private final List<Funcionario> dados = new ArrayList<>();

    @Override
    public void salvar(Funcionario funcionario) {
        dados.add(funcionario);
    }

    @Override
    public Funcionario buscarPorMatricula(String matricula) {
        return dados.stream()
            .filter(f -> f.getMatricula().equalsIgnoreCase(matricula))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        // em memoria: o objeto ja foi modificado via setters, nada a fazer
    }
}
