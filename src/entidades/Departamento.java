package entidades;

import enums.StatusEntidade;

public class Departamento {
    private int codigo;
    private String nome;
    private String descricao;
    private StatusEntidade status;

    public Departamento(int codigo, String nome, String descricao, StatusEntidade status) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public StatusEntidade getStatus() { return status; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(StatusEntidade status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Codigo: %d | Nome: %s | Descricao: %s | Status: %s",
                codigo, nome, descricao, status.getValor());
    }
}
