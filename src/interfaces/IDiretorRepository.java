package interfaces;

import entities.Diretor;
import java.util.List;

public interface IDiretorRepository {
    void salvar(Diretor diretor);
    Diretor buscarPorMatricula(String matricula);
    List<Diretor> listarTodos();
    boolean existeMatricula(String matricula);
}
