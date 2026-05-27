package regras;

import entidades.Departamento;
import entidades.Gerente;
import enums.StatusEntidade;
import erros.ErroValidacao;
import interfaces.IDepartamentoRepositorio;
import interfaces.IDiretorRegras;
import interfaces.IGerenteRepositorio;
import java.util.List;

// Service do Diretor - gerencia departamentos e gerentes
// Usa IoC: recebe os repositorios pelo construtor
public class DiretorRegras implements IDiretorRegras {

    private final IDepartamentoRepositorio deptoRepo;
    private final IGerenteRepositorio gerenteRepo;

    public DiretorRegras(IDepartamentoRepositorio deptoRepo, IGerenteRepositorio gerenteRepo) {
        this.deptoRepo = deptoRepo;
        this.gerenteRepo = gerenteRepo;
    }

    @Override
    public void cadastrarDepartamento(int codigo, String nome, String descricao, StatusEntidade status)
            throws ErroValidacao {
        if (deptoRepo.existeCodigo(codigo))
            throw new ErroValidacao("Ja existe um departamento com o codigo " + codigo + ".");
        deptoRepo.salvar(new Departamento(codigo, nome, descricao, status));
    }

    @Override
    public void alterarDepartamento(int codigo, String nome, String descricao, StatusEntidade status)
            throws ErroValidacao {
        Departamento d = deptoRepo.buscarPorCodigo(codigo);
        if (d == null) throw new ErroValidacao("Departamento nao encontrado.");
        d.setNome(nome);
        d.setDescricao(descricao);
        d.setStatus(status);
        deptoRepo.atualizar(d);
    }

    @Override
    public List<Departamento> listarDepartamentos() {
        return deptoRepo.listarTodos();
    }

    @Override
    public Departamento buscarDepartamento(int codigo) {
        return deptoRepo.buscarPorCodigo(codigo);
    }

    @Override
    public void cadastrarGerente(String matricula, String nome, int codigoDepto, StatusEntidade status)
            throws ErroValidacao {
        if (gerenteRepo.existeMatricula(matricula))
            throw new ErroValidacao("Ja existe um gerente com a matricula " + matricula + ".");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ErroValidacao("Departamento nao encontrado.");
        gerenteRepo.salvar(new Gerente(matricula, nome, dep, status));
    }

    @Override
    public void alterarGerente(String matricula, String nome, int codigoDepto, StatusEntidade status)
            throws ErroValidacao {
        Gerente g = gerenteRepo.buscarPorMatricula(matricula);
        if (g == null) throw new ErroValidacao("Gerente nao encontrado.");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ErroValidacao("Departamento nao encontrado.");
        g.setNome(nome);
        g.setDepartamento(dep);
        g.setStatus(status);
        gerenteRepo.atualizar(g);
    }

    @Override
    public List<Gerente> listarGerentes() {
        return gerenteRepo.listarTodos();
    }

    @Override
    public Gerente buscarGerente(String matricula) {
        return gerenteRepo.buscarPorMatricula(matricula);
    }
}
