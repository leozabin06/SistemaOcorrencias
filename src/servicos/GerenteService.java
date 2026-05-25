package servicos;

import entidades.*;
import enums.StatusOcorrencia;
import excecoes.ValidacaoException;
import interfaces.*;
import validadores.ValidacaoOcorrencia;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Service do Gerente - cadastra funcionarios e gerencia ocorrencias
// IoC: recebe repos via construtor / SRP: validacao delegada pro ValidacaoOcorrencia
public class GerenteService implements IGerenteService {

    private final IFuncionarioRepository funcRepo;
    private final IDepartamentoRepository deptoRepo;
    private final IOcorrenciaRepository ocorrenciaRepo;
    private final ValidacaoOcorrencia validacao;

    public GerenteService(IFuncionarioRepository funcRepo, IDepartamentoRepository deptoRepo,
                          IOcorrenciaRepository ocorrenciaRepo, ValidacaoOcorrencia validacao) {
        this.funcRepo = funcRepo;
        this.deptoRepo = deptoRepo;
        this.ocorrenciaRepo = ocorrenciaRepo;
        this.validacao = validacao;
    }

    @Override
    public void cadastrarFuncionario(String matricula, String nome, int codigoDepto, String status)
            throws ValidacaoException {
        if (funcRepo.existeMatricula(matricula))
            throw new ValidacaoException("Ja existe um funcionario com a matricula " + matricula + ".");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ValidacaoException("Departamento nao encontrado.");
        funcRepo.salvar(new Funcionario(matricula, nome, dep, status));
    }

    @Override
    public void alterarFuncionario(String matricula, String nome, int codigoDepto, String status)
            throws ValidacaoException {
        Funcionario f = funcRepo.buscarPorMatricula(matricula);
        if (f == null) throw new ValidacaoException("Funcionario nao encontrado.");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ValidacaoException("Departamento nao encontrado.");
        f.setNome(nome);
        f.setDepartamento(dep);
        f.setStatus(status);
        funcRepo.atualizar(f);
    }

    @Override
    public List<Funcionario> listarFuncionarios() {
        return funcRepo.listarTodos();
    }

    @Override
    public List<Funcionario> listarFuncionariosInformatica() {
        // filtra so os que sao do depto de informatica
        return funcRepo.listarTodos().stream()
            .filter(f -> validacao.ehDepartamentoInformatica(f.getDepartamento().getNome()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Departamento> listarDepartamentos() {
        return deptoRepo.listarTodos();
    }

    @Override
    public void registrarOcorrencia(Gerente gerente, String descricao, LocalDate dataOcorrencia,
                                     LocalDate dataLimite, String matriculaFuncAlocado)
            throws ValidacaoException {
        // validacoes R6
        validacao.validarDataOcorrencia(dataOcorrencia);
        validacao.validarDataLimite(dataLimite);

        Funcionario func = funcRepo.buscarPorMatricula(matriculaFuncAlocado);
        if (func == null) throw new ValidacaoException("Funcionario nao encontrado.");
        validacao.validarFuncionarioInformatica(func);

        int num = ocorrenciaRepo.proximoNumero();
        Ocorrencia nova = new Ocorrencia(num, descricao, dataOcorrencia,
            gerente.getDepartamento(), func, dataLimite);
        ocorrenciaRepo.salvar(nova);
    }

    @Override
    public void alterarOcorrencia(int numero, Gerente gerente, String novaDescricao,
                                   LocalDate novaDataLimite, String novaMatriculaFunc)
            throws ValidacaoException {
        Ocorrencia oc = buscarOcorrenciaDoGerente(numero, gerente);

        if (novaDescricao != null && !novaDescricao.isEmpty())
            oc.setDescricao(novaDescricao);

        if (novaDataLimite != null) {
            validacao.validarDataLimite(novaDataLimite);
            oc.setDataLimite(novaDataLimite);
        }

        if (novaMatriculaFunc != null && !novaMatriculaFunc.isEmpty()) {
            Funcionario f = funcRepo.buscarPorMatricula(novaMatriculaFunc);
            if (f == null) throw new ValidacaoException("Funcionario nao encontrado.");
            validacao.validarFuncionarioInformatica(f);
            oc.setFuncionarioAlocado(f);
        }
        ocorrenciaRepo.atualizar(oc);
    }

    @Override
    public void alterarStatusDefinitivo(int numero, Gerente gerente, StatusOcorrencia novoStatus)
            throws ValidacaoException {
        Ocorrencia oc = buscarOcorrenciaDoGerente(numero, gerente);
        oc.setStatusDefinitivo(novoStatus);
        ocorrenciaRepo.atualizar(oc);
    }

    @Override
    public List<Ocorrencia> listarOcorrenciasPorDepto(int codigoDepto) {
        return ocorrenciaRepo.listarPorDepartamento(codigoDepto);
    }

    // busca a ocorrencia e verifica se pertence ao depto do gerente
    private Ocorrencia buscarOcorrenciaDoGerente(int numero, Gerente gerente) throws ValidacaoException {
        Ocorrencia oc = ocorrenciaRepo.buscarPorNumero(numero);
        if (oc == null || oc.getDeptoReportante().getCodigo() != gerente.getDepartamento().getCodigo())
            throw new ValidacaoException("Ocorrencia #" + numero + " nao encontrada ou nao pertence ao seu departamento.");
        return oc;
    }
}
