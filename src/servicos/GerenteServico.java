package servicos;

import entidades.*;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import interfaces.*;
import validadores.ValidacaoOcorrencia;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Service do Gerente - cadastra funcionarios e gerencia ocorrencias
// IoC: recebe repos via construtor / SRP: validacao delegada pro ValidacaoOcorrencia
public class GerenteServico implements IGerenteServico {

    private final IFuncionarioRepositorio funcRepo;
    private final IDepartamentoRepositorio deptoRepo;
    private final IOcorrenciaRepositorio ocorrenciaRepo;
    private final ValidacaoOcorrencia validacao;

    public GerenteServico(IFuncionarioRepositorio funcRepo, IDepartamentoRepositorio deptoRepo,
                          IOcorrenciaRepositorio ocorrenciaRepo, ValidacaoOcorrencia validacao) {
        this.funcRepo = funcRepo;
        this.deptoRepo = deptoRepo;
        this.ocorrenciaRepo = ocorrenciaRepo;
        this.validacao = validacao;
    }

    @Override
    public void cadastrarFuncionario(String matricula, String nome, int codigoDepto, String status)
            throws ErroValidacao {
        if (funcRepo.existeMatricula(matricula))
            throw new ErroValidacao("Ja existe um funcionario com a matricula " + matricula + ".");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ErroValidacao("Departamento nao encontrado.");
        funcRepo.salvar(new Funcionario(matricula, nome, dep, status));
    }

    @Override
    public void alterarFuncionario(String matricula, String nome, int codigoDepto, String status)
            throws ErroValidacao {
        Funcionario f = funcRepo.buscarPorMatricula(matricula);
        if (f == null) throw new ErroValidacao("Funcionario nao encontrado.");
        Departamento dep = deptoRepo.buscarPorCodigo(codigoDepto);
        if (dep == null) throw new ErroValidacao("Departamento nao encontrado.");
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
            throws ErroValidacao {
        // validacoes R6
        validacao.validarDataOcorrencia(dataOcorrencia);
        validacao.validarDataLimite(dataLimite);

        Funcionario func = funcRepo.buscarPorMatricula(matriculaFuncAlocado);
        if (func == null) throw new ErroValidacao("Funcionario nao encontrado.");
        validacao.validarFuncionarioInformatica(func);

        int num = ocorrenciaRepo.proximoNumero();
        Ocorrencia nova = new Ocorrencia(num, descricao, dataOcorrencia,
            gerente.getDepartamento(), func, dataLimite);
        ocorrenciaRepo.salvar(nova);
    }

    @Override
    public void alterarOcorrencia(int numero, Gerente gerente, String novaDescricao,
                                   LocalDate novaDataLimite, String novaMatriculaFunc)
            throws ErroValidacao {
        Ocorrencia oc = buscarOcorrenciaDoGerente(numero, gerente);

        if (novaDescricao != null && !novaDescricao.isEmpty())
            oc.setDescricao(novaDescricao);

        if (novaDataLimite != null) {
            validacao.validarDataLimite(novaDataLimite);
            oc.setDataLimite(novaDataLimite);
        }

        if (novaMatriculaFunc != null && !novaMatriculaFunc.isEmpty()) {
            Funcionario f = funcRepo.buscarPorMatricula(novaMatriculaFunc);
            if (f == null) throw new ErroValidacao("Funcionario nao encontrado.");
            validacao.validarFuncionarioInformatica(f);
            oc.setFuncionarioAlocado(f);
        }
        ocorrenciaRepo.atualizar(oc);
    }

    @Override
    public void alterarStatusDefinitivo(int numero, Gerente gerente, StatusOcorrencia novoStatus)
            throws ErroValidacao {
        Ocorrencia oc = buscarOcorrenciaDoGerente(numero, gerente);
        oc.setStatusDefinitivo(novoStatus);
        ocorrenciaRepo.atualizar(oc);
    }

    @Override
    public List<Ocorrencia> listarOcorrenciasPorDepto(int codigoDepto) {
        return ocorrenciaRepo.listarPorDepartamento(codigoDepto);
    }

    // busca a ocorrencia e verifica se pertence ao depto do gerente
    private Ocorrencia buscarOcorrenciaDoGerente(int numero, Gerente gerente) throws ErroValidacao {
        Ocorrencia oc = ocorrenciaRepo.buscarPorNumero(numero);
        if (oc == null || oc.getDeptoReportante().getCodigo() != gerente.getDepartamento().getCodigo())
            throw new ErroValidacao("Ocorrencia #" + numero + " nao encontrada ou nao pertence ao seu departamento.");
        return oc;
    }
}
