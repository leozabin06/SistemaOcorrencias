package enums;

/**
 * OCP - Open/Closed Principle:
 * Novos status podem ser adicionados aqui sem alterar a logica central
 * dos servicos (GerenteService, FuncionarioService).
 */
public enum StatusOcorrencia {
    ABERTA("aberta"),
    ENCERRADA("encerrada");
    // Para adicionar novo status (ex: EM_ANDAMENTO), basta incluir aqui.
    // Nenhuma classe de servico precisa ser modificada.

    private final String valor;

    StatusOcorrencia(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
