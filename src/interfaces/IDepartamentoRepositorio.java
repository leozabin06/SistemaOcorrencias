package interfaces;

import entidades.Departamento;
import java.util.List;

public interface IDepartamentoRepositorio {
    void salvar(Departamento departamento);
    Departamento buscarPorCodigo(int codigo);
    List<Departamento> listarTodos();
    boolean existeCodigo(int codigo);
    void atualizar(Departamento departamento);
}
