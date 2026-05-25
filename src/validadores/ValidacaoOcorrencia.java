package validadores;

import entidades.Funcionario;
import erros.ErroValidacao;
import java.time.LocalDate;

// Classe responsavel por validar as regras de negocio das ocorrencias (R6)
// SRP: separada dos services pra manter cada classe com uma responsabilidade
public class ValidacaoOcorrencia {

    // R6: data da ocorrencia nao pode ser futura
    public void validarDataOcorrencia(LocalDate data) throws ErroValidacao {
        if (data.isAfter(LocalDate.now()))
            throw new ErroValidacao("A data da ocorrencia nao pode ser uma data futura. Data informada: " + data);
    }

    // R6: data limite tem que ser futura
    public void validarDataLimite(LocalDate dataLimite) throws ErroValidacao {
        if (!dataLimite.isAfter(LocalDate.now()))
            throw new ErroValidacao("A data limite deve ser uma data futura. Data informada: " + dataLimite);
    }

    // R6: funcionario alocado tem que ser do depto de informatica
    public void validarFuncionarioInformatica(Funcionario funcionario) throws ErroValidacao {
        if (!ehDepartamentoInformatica(funcionario.getDepartamento().getNome())) {
            throw new ErroValidacao(
                "O funcionario '" + funcionario.getNome() +
                "' pertence ao departamento '" + funcionario.getDepartamento().getNome() +
                "'. O funcionario alocado deve ser do departamento de Informatica.");
        }
    }

    // verifica se o nome do depto bate com informatica/TI
    public boolean ehDepartamentoInformatica(String nomeDepartamento) {
        String nome = normalizar(nomeDepartamento.trim());
        return nome.contains("informat") || nome.equals("ti")
            || nome.contains("t.i") || nome.contains("t.i.")
            || nome.contains("tecnologia da informacao")
            || nome.contains("tecnologia de informacao");
    }

    // remove acentos pra comparar
    private String normalizar(String texto) {
        return texto.toLowerCase()
            .replace("á", "a").replace("â", "a").replace("ã", "a")
            .replace("é", "e").replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o").replace("ô", "o")
            .replace("ú", "u").replace("ç", "c");
    }
}
