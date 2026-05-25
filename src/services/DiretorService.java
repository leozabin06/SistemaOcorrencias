package services;

import entities.Departamento;
import entities.Gerente;
import exceptions.ValidacaoException;
import interfaces.IDepartamentoRepository;
import interfaces.IDiretorService;
import interfaces.IGerenteRepository;
import java.util.List;

/**
 * IoC - Inversao de Controle:
 * DiretorService nao instancia suas dependencias.
 * Recebe IDepartamentoRepository e IGerenteRepository via construtor.
 * O controlador (App) decide qual implementacao concreta injetar.
 */
public class DiretorService implements IDiretorService {

    private final IDepartamentoRepository deptoRepo;
    private final IGerenteRepository gerenteRepo;

    public DiretorService(IDepartamentoRepository deptoRepo, IGerenteRepository gerenteRepo) {
        this.deptoRepo = deptoRepo;
        this.gerenteRepo = gerenteRepo;
    }

    // --- Departamentos (R1) ---

    @Override
    public void cadastrarDepartamento(int codigo, String nome, String descricao, String status)
            throws ValidacaoException {
        if (deptoRepo.existeCodigo(codigo)) {
            throw new ValidacaoException("Ja existe um departamento com o codigo " + codigo + ".");
        }
        deptoRepo.salvar(new Departamento(codigo, nome, descricao, status));
    }

    @Override
    public void alterarDepartamento(int codigo, String nome, String descricao, String status)
            throws ValidacaoException {
        Departamento d = deptoRepo.buscarPorCodigo(codigo);
        if (d == null) throw new ValidacaoException("Departamento nao encontrado.");
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

    // --- Gerentes (R3) ---

    @Override
    public void cadastrarGerente(String matricula, String nome, int codigoDepto, String status)
            throws ValidacaoException {
        if (gerenteRepo.existeMatricula(matricula)) {
            throw new ValidacaoException("Ja existe um gerente com a matricula " + matricula + ".");
        }
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ValidacaoException("Departamento nao encontrado.");
        gerenteRepo.salvar(new Gerente(matricula, nome, dep, status));
    }

    @Override
    public void alterarGerente(String matricula, String nome, int codigoDepto, String status)
            throws ValidacaoException {
        Gerente g = gerenteRepo.buscarPorMatricula(matricula);
        if (g == null) throw new ValidacaoException("Gerente nao encontrado.");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ValidacaoException("Departamento nao encontrado.");
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
