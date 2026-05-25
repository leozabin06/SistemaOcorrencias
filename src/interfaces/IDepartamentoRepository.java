package interfaces;

import entities.Departamento;
import java.util.List;

public interface IDepartamentoRepository {
    void salvar(Departamento departamento);
    Departamento buscarPorCodigo(int codigo);
    List<Departamento> listarTodos();
    boolean existeCodigo(int codigo);
    void atualizar(Departamento departamento);
}
