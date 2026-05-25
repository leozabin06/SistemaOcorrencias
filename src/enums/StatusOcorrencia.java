package enums;

// Status possiveis de uma ocorrencia (OCP - pode adicionar novos sem mexer nos services)
public enum StatusOcorrencia {
    ABERTA("aberta"),
    ENCERRADA("encerrada");

    private final String valor;

    StatusOcorrencia(String valor) {
        this.valor = valor;
    }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}
