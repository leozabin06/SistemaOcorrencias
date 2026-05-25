package validators;

import entities.Funcionario;
import exceptions.ValidacaoException;
import java.time.LocalDate;

/**
 * SRP - Single Responsibility Principle:
 * Esta classe tem uma unica responsabilidade: validar as regras de negocio
 * das ocorrencias definidas no requisito R6.
 * A logica de validacao fica isolada das classes de persistencia e de servico.
 */
public class ValidacaoOcorrencia {

    /**
     * R6: A data da ocorrencia nao pode ser uma data futura.
     */
    public void validarDataOcorrencia(LocalDate data) throws ValidacaoException {
        if (data.isAfter(LocalDate.now())) {
            throw new ValidacaoException(
                "A data da ocorrencia nao pode ser uma data futura. Data informada: " + data);
        }
    }

    /**
     * R6: A data limite para solucao deve ser obrigatoriamente uma data futura.
     */
    public void validarDataLimite(LocalDate dataLimite) throws ValidacaoException {
        if (!dataLimite.isAfter(LocalDate.now())) {
            throw new ValidacaoException(
                "A data limite deve ser uma data futura. Data informada: " + dataLimite);
        }
    }

    /**
     * R6: O funcionario alocado deve pertencer ao departamento de Informatica.
     */
    public void validarFuncionarioInformatica(Funcionario funcionario) throws ValidacaoException {
        if (!ehDepartamentoInformatica(funcionario.getDepartamento().getNome())) {
            throw new ValidacaoException(
                "O funcionario '" + funcionario.getNome() +
                "' pertence ao departamento '" + funcionario.getDepartamento().getNome() +
                "'. O funcionario alocado deve ser do departamento de Informatica.");
        }
    }

    /**
     * Verifica se um nome de departamento corresponde ao departamento de Informatica.
     * Usado tambem para filtragem em outros servicos.
     */
    public boolean ehDepartamentoInformatica(String nomeDepartamento) {
        String normalizado = normalizar(nomeDepartamento.trim());
        return normalizado.contains("informat")
            || normalizado.equals("ti")
            || normalizado.contains("t.i")
            || normalizado.contains("t.i.")
            || normalizado.contains("tecnologia da informacao")
            || normalizado.contains("tecnologia de informacao");
    }

    private String normalizar(String texto) {
        return texto.toLowerCase()
            .replace("á", "a").replace("â", "a").replace("ã", "a")
            .replace("é", "e").replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o").replace("ô", "o")
            .replace("ú", "u")
            .replace("ç", "c");
    }
}
