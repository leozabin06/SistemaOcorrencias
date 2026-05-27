package entidades;

import enums.StatusEntidade;

public class Diretor {
    private String matricula;
    private String nome;
    private StatusEntidade status;

    public Diretor(String matricula, String nome, StatusEntidade status) {
        this.matricula = matricula;
        this.nome = nome;
        this.status = status;
    }

    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public StatusEntidade getStatus() { return status; }

    public void setNome(String nome) { this.nome = nome; }
    public void setStatus(StatusEntidade status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Matricula: %s | Nome: %s | Status: %s",
                matricula, nome, status.getValor());
    }
}
