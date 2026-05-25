package interfaces;

import entidades.Ocorrencia;
import java.util.List;

public interface IOcorrenciaRepository {
    void salvar(Ocorrencia ocorrencia);
    Ocorrencia buscarPorNumero(int numero);
    List<Ocorrencia> listarPorDepartamento(int codigoDepto);
    List<Ocorrencia> listarPorFuncionario(String matricula);
    int proximoNumero();
    void atualizar(Ocorrencia ocorrencia);
}
