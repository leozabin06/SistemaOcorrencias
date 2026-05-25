package interfaces;

import entidades.Diretor;
import java.util.List;

public interface IDiretorRepositorio {
    void salvar(Diretor diretor);
    Diretor buscarPorMatricula(String matricula);
    List<Diretor> listarTodos();
    boolean existeMatricula(String matricula);
}
