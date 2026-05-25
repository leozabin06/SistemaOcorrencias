package ui;

import entities.Funcionario;
import entities.Ocorrencia;
import enums.StatusOcorrencia;
import exceptions.ValidacaoException;
import interfaces.IFuncionarioService;
import java.util.List;
import java.util.Scanner;

/**
 * Tela do Funcionario (R5 - prototipacao):
 * - Visualizar apenas ocorrencias atribuidas a ele
 * - Alterar STATUS TEMPORARIO das suas ocorrencias
 * O funcionario NAO pode: registrar ocorrencias, alterar status definitivo,
 * gerenciar departamentos ou funcionarios.
 */
public class MenuFuncionario {

    private final IFuncionarioService service;
    private final Scanner sc;

    public MenuFuncionario(IFuncionarioService service, Scanner sc) {
        this.service = service;
        this.sc = sc;
    }

    public void exibir(Funcionario funcionario) {
        int op;
        do {
            System.out.println("\n========================================");
            System.out.println("  MENU FUNCIONARIO  [" + funcionario.getNome()
                + " - " + funcionario.getDepartamento().getNome() + "]");
            System.out.println("========================================");
            System.out.println("  1. Visualizar Minhas Ocorrencias");
            System.out.println("  2. Alterar Status Temporario de Ocorrencia");
            System.out.println("  0. Voltar");
            System.out.println("========================================");
            op = lerInt("Opcao: ");
            try {
                switch (op) {
                    case 1 -> visualizarOcorrencias(funcionario);
                    case 2 -> alterarStatusTemporario(funcionario);
                    case 0 -> System.out.println("Voltando ao menu principal...");
                    default -> System.out.println("[!] Opcao invalida.");
                }
            } catch (ValidacaoException e) {
                System.out.println("[ERRO] " + e.getMessage());
            }
        } while (op != 0);
    }

    private void visualizarOcorrencias(Funcionario funcionario) {
        System.out.println("\n-- Minhas Ocorrencias --");
        List<Ocorrencia> lista = service.listarMinhasOcorrencias(funcionario.getMatricula());
        if (lista.isEmpty()) {
            System.out.println("Nenhuma ocorrencia atribuida a voce no momento.");
            return;
        }
        System.out.println("--------------------------------------------");
        lista.forEach(o -> { System.out.println(o); System.out.println("--------------------------------------------"); });
    }

    /**
     * R5 - Limitacao do Funcionario:
     * O funcionario pode alterar APENAS o status temporario.
     * O status definitivo e exclusivo do gerente.
     */
    private void alterarStatusTemporario(Funcionario funcionario) throws ValidacaoException {
        List<Ocorrencia> lista = service.listarMinhasOcorrencias(funcionario.getMatricula());
        if (lista.isEmpty()) {
            System.out.println("Nenhuma ocorrencia atribuida a voce.");
            return;
        }
        System.out.println("\n-- Minhas Ocorrencias --");
        System.out.println("--------------------------------------------");
        lista.forEach(o -> { System.out.println(o); System.out.println("--------------------------------------------"); });

        int num = lerInt("Numero da ocorrencia: ");
        System.out.println("Novo status temporario:");
        System.out.println("  1. Aberta");
        System.out.println("  2. Encerrada");
        int op = lerInt("Opcao: ");

        StatusOcorrencia novoStatus = switch (op) {
            case 1 -> StatusOcorrencia.ABERTA;
            case 2 -> StatusOcorrencia.ENCERRADA;
            default -> throw new ValidacaoException("Opcao de status invalida.");
        };

        service.alterarStatusTemporario(num, funcionario.getMatricula(), novoStatus);
        System.out.println("[OK] Status temporario atualizado para: " + novoStatus.getValor());
        System.out.println("     (O status definitivo permanece sob responsabilidade do gerente.)");
    }

    private int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("[!] Digite um numero inteiro valido."); }
        }
    }
}
