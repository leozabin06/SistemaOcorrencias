package entities;

public class Gerente {
    private String matricula;
    private String nome;
    private Departamento departamento;
    private String status;

    public Gerente(String matricula, String nome, Departamento departamento, String status) {
        this.matricula = matricula;
        this.nome = nome;
        this.departamento = departamento;
        this.status = status;
    }

    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public Departamento getDepartamento() { return departamento; }
    public String getStatus() { return status; }

    public void setNome(String nome) { this.nome = nome; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Matricula: %s | Nome: %s | Departamento: %s | Status: %s",
                matricula, nome, departamento.getNome(), status);
    }
}
