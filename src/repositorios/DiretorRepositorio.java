package repositorios;

import entidades.Diretor;
import interfaces.IDiretorRepositorio;
import java.util.ArrayList;
import java.util.List;

public class DiretorRepositorio implements IDiretorRepositorio {

    private final List<Diretor> dados = new ArrayList<>();

    @Override
    public void salvar(Diretor diretor) {
        dados.add(diretor);
    }

    @Override
    public Diretor buscarPorMatricula(String matricula) {
        return dados.stream()
            .filter(d -> d.getMatricula().equalsIgnoreCase(matricula))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Diretor> listarTodos() {
        return new ArrayList<>(dados);
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }
}
