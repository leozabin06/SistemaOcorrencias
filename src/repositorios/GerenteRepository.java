package repositorios;

import entidades.Gerente;
import interfaces.IGerenteRepository;
import java.util.ArrayList;
import java.util.List;

public class GerenteRepository implements IGerenteRepository {

    private final List<Gerente> dados = new ArrayList<>();

    @Override
    public void salvar(Gerente gerente) {
        dados.add(gerente);
    }

    @Override
    public Gerente buscarPorMatricula(String matricula) {
        return dados.stream()
            .filter(g -> g.getMatricula().equalsIgnoreCase(matricula))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Gerente> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }

    @Override
    public void atualizar(Gerente gerente) {
        // em memoria: o objeto ja foi modificado via setters, nada a fazer
    }
}
