package interfaces;

import entities.Gerente;
import java.util.List;

public interface IGerenteRepository {
    void salvar(Gerente gerente);
    Gerente buscarPorMatricula(String matricula);
    List<Gerente> listarTodos();
    boolean existeMatricula(String matricula);
    void atualizar(Gerente gerente);
}
