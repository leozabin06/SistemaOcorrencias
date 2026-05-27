package enums;

public enum StatusEntidade {
    ATIVO("ativo"),
    INATIVO("inativo");

    private final String valor;

    StatusEntidade(String valor) {
        this.valor = valor;
    }

    public String getValor() { return valor; }

    public static StatusEntidade fromValor(String valor) {
        for (StatusEntidade s : values()) {
            if (s.valor.equalsIgnoreCase(valor)) return s;
        }
        throw new IllegalArgumentException("Status invalido: " + valor);
    }

    @Override
    public String toString() { return valor; }
}
