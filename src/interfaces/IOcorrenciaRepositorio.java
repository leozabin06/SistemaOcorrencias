package interfaces;

import entidades.Ocorrencia;
import java.util.List;

public interface IOcorrenciaRepositorio {
    void salvar(Ocorrencia ocorrencia);
    Ocorrencia buscarPorNumero(int numero);
    List<Ocorrencia> listarPorDepartamento(int codigoDepto);
    List<Ocorrencia> listarPorFuncionario(String matricula);
    int proximoNumero();
    void atualizar(Ocorrencia ocorrencia);
}
