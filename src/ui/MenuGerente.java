package ui;

import entidades.Departamento;
import entidades.Funcionario;
import entidades.Gerente;
import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import interfaces.IGerenteRegras;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Tela do Gerente (R5 - prototipacao):
 * - Registrar ocorrencias
 * - Alterar descricao, data limite e funcionario alocado
 * - Encerrar ocorrencia (alterar STATUS DEFINITIVO) - exclusivo do gerente
 * O gerente NAO pode alterar o status temporario (limitacao R5).
 */
public class MenuGerente {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final IGerenteRegras regras;
    private final Scanner sc;

    public MenuGerente(IGerenteRegras regras, Scanner sc) {
        this.regras = service;
        this.sc = sc;
    }

    public void exibir(Gerente gerente) {
        int op;
        do {
            System.out.println("\n========================================");
            System.out.println("  MENU GERENTE  [" + gerente.getNome()
                + " - " + gerente.getDepartamento().getNome() + "]");
            System.out.println("========================================");
            System.out.println("  FUNCIONARIOS");
            System.out.println("  1. Cadastrar Funcionario");
            System.out.println("  2. Alterar Funcionario");
            System.out.println("  3. Listar Funcionarios");
            System.out.println("  OCORRENCIAS");
            System.out.println("  4. Registrar Ocorrencia");
            System.out.println("  5. Alterar Ocorrencia");
            System.out.println("  6. Encerrar Ocorrencia (Status Definitivo)");
            System.out.println("  7. Visualizar Ocorrencias do Departamento");
            System.out.println("  0. Voltar");
            System.out.println("========================================");
            op = lerInt("Opcao: ");
            try {
                switch (op) {
                    case 1 -> cadastrarFuncionario();
                    case 2 -> alterarFuncionario();
                    case 3 -> listarFuncionarios();
                    case 4 -> registrarOcorrencia(gerente);
                    case 5 -> alterarOcorrencia(gerente);
                    case 6 -> alterarStatusDefinitivo(gerente);
                    case 7 -> visualizarOcorrencias(gerente);
                    case 0 -> System.out.println("Voltando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida.");
                }
            } catch (ErroValidacao e) {
                System.out.println("[ERRO] " + e.getMessage());
            }
        } while (op != 0);
    }

    // ---- Funcionarios ----

    private void cadastrarFuncionario() throws ErroValidacao {
        System.out.println("\n-- Cadastrar Funcionario --");
        String mat    = lerString("Matricula: ");
        String nome   = lerString("Nome: ");
        listarDepartamentos();
        int cod       = lerInt("Codigo do departamento: ");
        String status = selecionarStatusAtivo();
        regras.cadastrarFuncionario(mat, nome, cod, status);
        System.out.println("[OK] Funcionario cadastrado com sucesso.");
    }

    private void alterarFuncionario() throws ErroValidacao {
        listarFuncionarios();
        if (regras.listarFuncionarios().isEmpty()) return;

        String mat = lerString("Matricula do funcionario a alterar: ");
        Funcionario atual = regras.listarFuncionarios().stream()
            .filter(f -> f.getMatricula().equalsIgnoreCase(mat))
            .findFirst().orElse(null);
        if (atual == null) { System.out.println("[!] Funcionario nao encontrado."); return; }

        System.out.println("Deixe em branco para manter o valor atual.");
        String nome = lerOpcional("Novo nome [" + atual.getNome() + "]: ", atual.getNome());

        listarDepartamentos();
        int codAtual = atual.getDepartamento().getCodigo();
        int cod = lerInt("Codigo do departamento (0 para manter [" + codAtual + "]): ");
        if (cod == 0) cod = codAtual;

        String status = selecionarStatusAtivoOpcional(atual.getStatus());
        regras.alterarFuncionario(mat, nome, cod, status);
        System.out.println("[OK] Funcionario alterado com sucesso.");
    }

    private void listarFuncionarios() {
        System.out.println("\n-- Funcionarios Cadastrados --");
        List<Funcionario> lista = regras.listarFuncionarios();
        if (lista.isEmpty()) { System.out.println("Nenhum funcionario cadastrado."); return; }
        lista.forEach(f -> System.out.println("  " + f));
    }

    private void listarDepartamentos() {
        System.out.println("\n-- Departamentos Disponiveis --");
        List<Departamento> lista = regras.listarDepartamentos();
        if (lista.isEmpty()) { System.out.println("Nenhum departamento cadastrado."); return; }
        lista.forEach(d -> System.out.println("  " + d));
    }

    // ---- Ocorrencias ----

    private void registrarOcorrencia(Gerente gerente) throws ErroValidacao {
        System.out.println("\n-- Registrar Ocorrencia --");
        System.out.println("Departamento reportante: " + gerente.getDepartamento().getNome());

        String desc = lerString("Descricao: ");

        // R6: data da ocorrencia nao pode ser futura
        LocalDate dataOcorrencia = lerDataComValidacao(
            "Data da ocorrencia (dd/MM/yyyy) [nao pode ser futura]: ",
            false);

        // R6: data limite deve ser futura
        LocalDate dataLimite = lerDataComValidacao(
            "Data limite para solucao (dd/MM/yyyy) [deve ser futura]: ",
            true);

        // R6: funcionario alocado deve ser do depto de informatica
        System.out.println("\nFuncionarios disponiveis (departamento de Informatica):");
        List<Funcionario> funcInfo = regras.listarFuncionariosInformatica();
        if (funcInfo.isEmpty()) {
            System.out.println("[!] Nenhum funcionario do departamento de Informatica encontrado.");
            System.out.println("    Cadastre um funcionario do depto de Informatica primeiro.");
            return;
        }
        funcInfo.forEach(f -> System.out.println("  " + f));

        String matriculaFunc = lerString("Matricula do funcionario alocado: ");
        regras.registrarOcorrencia(gerente, desc, dataOcorrencia, dataLimite, matriculaFunc);
        System.out.println("[OK] Ocorrencia registrada com sucesso.");
    }

    private void alterarOcorrencia(Gerente gerente) throws ErroValidacao {
        visualizarOcorrencias(gerente);
        List<Ocorrencia> lista = regras.listarOcorrenciasPorDepto(gerente.getDepartamento().getCodigo());
        if (lista.isEmpty()) return;

        int num = lerInt("Numero da ocorrencia a alterar: ");
        Ocorrencia atual = lista.stream().filter(o -> o.getNumero() == num).findFirst().orElse(null);
        if (atual == null) { System.out.println("[!] Ocorrencia nao encontrada."); return; }

        System.out.println("\nO que deseja alterar?");
        System.out.println("  1. Descricao");
        System.out.println("  2. Data Limite");
        System.out.println("  3. Funcionario Alocado");
        System.out.println("  0. Cancelar");
        int op = lerInt("Opcao: ");

        switch (op) {
            case 1 -> {
                String novaDesc = lerString("Nova descricao: ");
                regras.alterarOcorrencia(num, gerente, novaDesc, null, null);
                System.out.println("[OK] Descricao atualizada.");
            }
            case 2 -> {
                LocalDate novaData = lerDataComValidacao(
                    "Nova data limite (dd/MM/yyyy) [deve ser futura]: ", true);
                regras.alterarOcorrencia(num, gerente, null, novaData, null);
                System.out.println("[OK] Data limite atualizada.");
            }
            case 3 -> {
                System.out.println("Funcionarios do departamento de Informatica:");
                regras.listarFuncionariosInformatica()
                    .forEach(f -> System.out.println("  " + f));
                String novaMatricula = lerString("Matricula do novo funcionario: ");
                regras.alterarOcorrencia(num, gerente, null, null, novaMatricula);
                System.out.println("[OK] Funcionario alocado atualizado.");
            }
            case 0 -> System.out.println("Operacao cancelada.");
            default -> System.out.println("[!] Opcao invalida.");
        }
    }

    /**
     * R5 - Tela exclusiva do Gerente: encerramento definitivo da ocorrencia.
     * O funcionario NAO tem acesso a esta opcao.
     */
    private void alterarStatusDefinitivo(Gerente gerente) throws ErroValidacao {
        visualizarOcorrencias(gerente);
        List<Ocorrencia> lista = regras.listarOcorrenciasPorDepto(gerente.getDepartamento().getCodigo());
        if (lista.isEmpty()) return;

        int num = lerInt("Numero da ocorrencia: ");
        System.out.println("Novo status definitivo:");
        System.out.println("  1. Aberta");
        System.out.println("  2. Encerrada");
        int op = lerInt("Opcao: ");

        StatusOcorrencia novoStatus = switch (op) {
            case 1 -> StatusOcorrencia.ABERTA;
            case 2 -> StatusOcorrencia.ENCERRADA;
            default -> throw new ErroValidacao("Opcao de status invalida.");
        };

        regras.alterarStatusDefinitivo(num, gerente, novoStatus);
        System.out.println("[OK] Status definitivo atualizado para: " + novoStatus.getValor());
    }

    private void visualizarOcorrencias(Gerente gerente) {
        System.out.println("\n-- Ocorrencias do Departamento: "
            + gerente.getDepartamento().getNome() + " --");
        List<Ocorrencia> lista = regras.listarOcorrenciasPorDepto(
            gerente.getDepartamento().getCodigo());
        if (lista.isEmpty()) {
            System.out.println("Nenhuma ocorrencia registrada para este departamento.");
            return;
        }
        imprimirOcorrencias(lista);
    }

    // ---- Helpers de entrada ----

    /**
     * Le uma data e valida se e futura (futura=true) ou nao futura (futura=false).
     * Repete ate o usuario digitar uma data valida.
     */
    private LocalDate lerDataComValidacao(String prompt, boolean deveFuturo) {
        while (true) {
            System.out.print(prompt);
            String entrada = sc.nextLine().trim();
            try {
                LocalDate data = LocalDate.parse(entrada, FMT);
                if (deveFuturo && !data.isAfter(LocalDate.now())) {
                    System.out.println("[!] A data deve ser uma data futura. Tente novamente.");
                    continue;
                }
                if (!deveFuturo && data.isAfter(LocalDate.now())) {
                    System.out.println("[!] A data da ocorrencia nao pode ser uma data futura. Tente novamente.");
                    continue;
                }
                return data;
            } catch (DateTimeParseException e) {
                System.out.println("[!] Formato invalido. Use dd/MM/yyyy.");
            }
        }
    }

    private String selecionarStatusAtivo() {
        System.out.println("Status:  1. Ativo   2. Inativo");
        int op = lerInt("Opcao: ");
        return (op == 1) ? "ativo" : "inativo";
    }

    private String selecionarStatusAtivoOpcional(String atual) {
        System.out.println("Status:  1. Ativo   2. Inativo   0. Manter [" + atual + "]");
        int op = lerInt("Opcao: ");
        if (op == 1) return "ativo";
        if (op == 2) return "inativo";
        return atual;
    }

    private String lerOpcional(String prompt, String valorAtual) {
        System.out.print(prompt);
        String val = sc.nextLine().trim();
        return val.isEmpty() ? valorAtual : val;
    }

    private int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("[!] Digite um numero inteiro valido."); }
        }
    }

    private String lerString(String prompt) {
        String val = "";
        while (val.isEmpty()) {
            System.out.print(prompt);
            val = sc.nextLine().trim();
            if (val.isEmpty()) System.out.println("[!] Campo obrigatorio.");
        }
        return val;
    }

    private void imprimirOcorrencias(List<Ocorrencia> lista) {
        System.out.println("--------------------------------------------");
        lista.forEach(o -> { System.out.println(o); System.out.println("--------------------------------------------"); });
    }
}
