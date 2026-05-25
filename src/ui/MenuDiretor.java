package ui;

import entities.Departamento;
import entities.Gerente;
import exceptions.ValidacaoException;
import interfaces.IDiretorService;
import java.util.List;
import java.util.Scanner;

public class MenuDiretor {

    private final IDiretorService service;
    private final Scanner sc;

    public MenuDiretor(IDiretorService service, Scanner sc) {
        this.service = service;
        this.sc = sc;
    }

    public void exibir(String nomeDiretor) {
        int op;
        do {
            System.out.println("\n========================================");
            System.out.println("  MENU DIRETOR  [" + nomeDiretor + "]");
            System.out.println("========================================");
            System.out.println("  DEPARTAMENTOS");
            System.out.println("  1. Cadastrar Departamento");
            System.out.println("  2. Alterar Departamento");
            System.out.println("  3. Listar Departamentos");
            System.out.println("  GERENTES");
            System.out.println("  4. Cadastrar Gerente");
            System.out.println("  5. Alterar Gerente");
            System.out.println("  6. Listar Gerentes");
            System.out.println("  0. Voltar");
            System.out.println("========================================");
            op = lerInt("Opcao: ");
            try {
                switch (op) {
                    case 1 -> cadastrarDepartamento();
                    case 2 -> alterarDepartamento();
                    case 3 -> listarDepartamentos();
                    case 4 -> cadastrarGerente();
                    case 5 -> alterarGerente();
                    case 6 -> listarGerentes();
                    case 0 -> System.out.println("Voltando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida.");
                }
            } catch (ValidacaoException e) {
                System.out.println("[ERRO] " + e.getMessage());
            }
        } while (op != 0);
    }

    // ---- Departamentos ----

    private void cadastrarDepartamento() throws ValidacaoException {
        System.out.println("\n-- Cadastrar Departamento --");
        int cod       = lerInt("Codigo: ");
        String nome   = lerString("Nome: ");
        String desc   = lerString("Descricao: ");
        String status = selecionarStatusAtivo();
        service.cadastrarDepartamento(cod, nome, desc, status);
        System.out.println("[OK] Departamento cadastrado com sucesso.");
    }

    private void alterarDepartamento() throws ValidacaoException {
        listarDepartamentos();
        List<Departamento> lista = service.listarDepartamentos();
        if (lista.isEmpty()) return;

        int cod = lerInt("Codigo do departamento a alterar: ");
        Departamento atual = service.buscarDepartamento(cod);
        if (atual == null) { System.out.println("[!] Departamento nao encontrado."); return; }

        System.out.println("Deixe em branco para manter o valor atual.");
        String nome   = lerOpcional("Novo nome [" + atual.getNome() + "]: ", atual.getNome());
        String desc   = lerOpcional("Nova descricao [" + atual.getDescricao() + "]: ", atual.getDescricao());
        String status = selecionarStatusAtivoOpcional(atual.getStatus());

        service.alterarDepartamento(cod, nome, desc, status);
        System.out.println("[OK] Departamento alterado com sucesso.");
    }

    private void listarDepartamentos() {
        System.out.println("\n-- Departamentos Cadastrados --");
        List<Departamento> lista = service.listarDepartamentos();
        if (lista.isEmpty()) { System.out.println("Nenhum departamento cadastrado."); return; }
        lista.forEach(d -> System.out.println("  " + d));
    }

    // ---- Gerentes ----

    private void cadastrarGerente() throws ValidacaoException {
        System.out.println("\n-- Cadastrar Gerente --");
        if (service.listarDepartamentos().isEmpty()) {
            System.out.println("[!] Nenhum departamento cadastrado. Cadastre um departamento primeiro.");
            return;
        }
        String mat  = lerString("Matricula: ");
        String nome = lerString("Nome: ");
        listarDepartamentos();
        int cod     = lerInt("Codigo do departamento que gerencia: ");
        String status = selecionarStatusAtivo();
        service.cadastrarGerente(mat, nome, cod, status);
        System.out.println("[OK] Gerente cadastrado com sucesso.");
    }

    private void alterarGerente() throws ValidacaoException {
        listarGerentes();
        List<Gerente> lista = service.listarGerentes();
        if (lista.isEmpty()) return;

        String mat  = lerString("Matricula do gerente a alterar: ");
        Gerente atual = service.buscarGerente(mat);
        if (atual == null) { System.out.println("[!] Gerente nao encontrado."); return; }

        System.out.println("Deixe em branco para manter o valor atual.");
        String nome = lerOpcional("Novo nome [" + atual.getNome() + "]: ", atual.getNome());

        listarDepartamentos();
        int codAtual = atual.getDepartamento().getCodigo();
        int cod = lerInt("Codigo do departamento (0 para manter [" + codAtual + "]): ");
        if (cod == 0) cod = codAtual;

        String status = selecionarStatusAtivoOpcional(atual.getStatus());
        service.alterarGerente(mat, nome, cod, status);
        System.out.println("[OK] Gerente alterado com sucesso.");
    }

    private void listarGerentes() {
        System.out.println("\n-- Gerentes Cadastrados --");
        List<Gerente> lista = service.listarGerentes();
        if (lista.isEmpty()) { System.out.println("Nenhum gerente cadastrado."); return; }
        lista.forEach(g -> System.out.println("  " + g));
    }

    // ---- Helpers de entrada ----

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
}
