package entities;

public class Departamento {
    private int codigo;
    private String nome;
    private String descricao;
    private String status;

    public Departamento(int codigo, String nome, String descricao, String status) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Codigo: %d | Nome: %s | Descricao: %s | Status: %s",
                codigo, nome, descricao, status);
    }
}
