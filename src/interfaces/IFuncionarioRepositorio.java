package interfaces;

import entidades.Funcionario;
import java.util.List;

public interface IFuncionarioRepositorio {
    void salvar(Funcionario funcionario);
    Funcionario buscarPorMatricula(String matricula);
    List<Funcionario> listarTodos();
    boolean existeMatricula(String matricula);
    void atualizar(Funcionario funcionario);
}
