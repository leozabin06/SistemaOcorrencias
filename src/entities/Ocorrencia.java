package entities;

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

    /** R5: alteravel apenas pelo funcionario alocado */
    private StatusOcorrencia statusTemporario;

    /** R5: alteravel apenas pelo gerente do departamento reportante */
    private StatusOcorrencia statusDefinitivo;

    public Ocorrencia(int numero, String descricao, LocalDate dataOcorrencia,
                      Departamento deptoReportante, Funcionario funcionarioAlocado,
                      LocalDate dataLimite) {
        this.numero             = numero;
        this.descricao          = descricao;
        this.dataOcorrencia     = dataOcorrencia;
        this.deptoReportante    = deptoReportante;
        this.funcionarioAlocado = funcionarioAlocado;
        this.dataLimite         = dataLimite;
        this.statusTemporario   = StatusOcorrencia.ABERTA;
        this.statusDefinitivo   = StatusOcorrencia.ABERTA;
    }

    public int getNumero()                        { return numero; }
    public String getDescricao()                  { return descricao; }
    public LocalDate getDataOcorrencia()          { return dataOcorrencia; }
    public Departamento getDeptoReportante()      { return deptoReportante; }
    public Funcionario getFuncionarioAlocado()    { return funcionarioAlocado; }
    public LocalDate getDataLimite()              { return dataLimite; }
    public StatusOcorrencia getStatusTemporario() { return statusTemporario; }
    public StatusOcorrencia getStatusDefinitivo() { return statusDefinitivo; }

    public void setDescricao(String descricao)                       { this.descricao = descricao; }
    public void setFuncionarioAlocado(Funcionario funcionarioAlocado){ this.funcionarioAlocado = funcionarioAlocado; }
    public void setDataLimite(LocalDate dataLimite)                  { this.dataLimite = dataLimite; }
    public void setStatusTemporario(StatusOcorrencia status)         { this.statusTemporario = status; }
    public void setStatusDefinitivo(StatusOcorrencia status)         { this.statusDefinitivo = status; }

    @Override
    public String toString() {
        return String.format(
            "Numero: %d\n" +
            "  Descricao        : %s\n" +
            "  Data Ocorrencia  : %s\n" +
            "  Depto Reportante : [%d] %s\n" +
            "  Func. Alocado    : [%s] %s\n" +
            "  Data Limite      : %s\n" +
            "  Status Temporario: %s  (alteravel pelo funcionario)\n" +
            "  Status Definitivo: %s  (alteravel pelo gerente)",
            numero,
            descricao,
            dataOcorrencia.format(FMT),
            deptoReportante.getCodigo(), deptoReportante.getNome(),
            funcionarioAlocado.getMatricula(), funcionarioAlocado.getNome(),
            dataLimite.format(FMT),
            statusTemporario.getValor(),
            statusDefinitivo.getValor());
    }
}
