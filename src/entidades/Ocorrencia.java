package entidades;

import enums.StatusOcorrencia;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ocorrencia {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int numero;
    private String descricao;
    private final LocalDate dataOcorrencia;
    private final Departamento deptoReportante;
    private Funcionario funcionarioAlocado;
    private LocalDate dataLimite;
    private StatusOcorrencia statusTemporario; // R5: so o funcionario alocado altera
    private StatusOcorrencia statusDefinitivo; // R5: so o gerente do depto altera

    public Ocorrencia(int numero, String descricao, LocalDate dataOcorrencia,
                      Departamento deptoReportante, Funcionario funcionarioAlocado,
                      LocalDate dataLimite) {
        this.numero = numero;
        this.descricao = descricao;
        this.dataOcorrencia = dataOcorrencia;
        this.deptoReportante = deptoReportante;
        this.funcionarioAlocado = funcionarioAlocado;
        this.dataLimite = dataLimite;
        this.statusTemporario = StatusOcorrencia.ABERTA;
        this.statusDefinitivo = StatusOcorrencia.ABERTA;
    }

    // getters
    public int getNumero() { return numero; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataOcorrencia() { return dataOcorrencia; }
    public Departamento getDeptoReportante() { return deptoReportante; }
    public Funcionario getFuncionarioAlocado() { return funcionarioAlocado; }
    public LocalDate getDataLimite() { return dataLimite; }
    public StatusOcorrencia getStatusTemporario() { return statusTemporario; }
    public StatusOcorrencia getStatusDefinitivo() { return statusDefinitivo; }

    // setters
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setFuncionarioAlocado(Funcionario func) { this.funcionarioAlocado = func; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }
    public void setStatusTemporario(StatusOcorrencia status) { this.statusTemporario = status; }
    public void setStatusDefinitivo(StatusOcorrencia status) { this.statusDefinitivo = status; }

    @Override
    public String toString() {
        return "Ocorrencia #" + numero + " - " + descricao
            + " | Data: " + dataOcorrencia.format(FMT)
            + " | Depto: " + deptoReportante.getNome()
            + " | Func: " + funcionarioAlocado.getNome()
            + " | Limite: " + dataLimite.format(FMT)
            + " | Temp: " + statusTemporario.getValor()
            + " | Def: " + statusDefinitivo.getValor();
    }
}
