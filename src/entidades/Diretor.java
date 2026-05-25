package entidades;

public class Diretor {
    private String matricula;
    private String nome;
    private String status;

    public Diretor(String matricula, String nome, String status) {
        this.matricula = matricula;
        this.nome = nome;
        this.status = status;
    }

    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public String getStatus() { return status; }

    public void setNome(String nome) { this.nome = nome; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Matricula: %s | Nome: %s | Status: %s",
                matricula, nome, status);
    }
}
