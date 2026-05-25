package ui.swing;

import entidades.Funcionario;
import entidades.Gerente;
import interfaces.*;

import javax.swing.*;
import java.awt.*;

// Janela principal - alterna entre as telas usando CardLayout
public class TelaPrincipal extends JFrame {

    // cores do sistema
    public static final Color COR_FUNDO = new Color(245, 247, 250);
    public static final Color COR_CABECALHO = new Color(30, 60, 120);
    public static final Color COR_TEXTO = new Color(20, 20, 20);
    public static final Color COR_TEXTO_CLARO = Color.WHITE;
    public static final Color COR_BOTAO_AZUL = new Color(33, 97, 179);
    public static final Color COR_BOTAO_VERDE = new Color(36, 120, 60);
    public static final Color COR_BOTAO_VERM = new Color(180, 30, 30);
    public static final Color COR_BOTAO_CINZA = new Color(90, 90, 100);
    public static final Color COR_LINHA_PAR = new Color(235, 242, 255);
    public static final Color COR_SELECAO = new Color(180, 210, 255);
    public static final Color COR_BORDA = new Color(180, 190, 210);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    private final IDiretorRegras diretorRegras;
    private final IGerenteRegras gerenteRegras;
    private final IFuncionarioRegras funcRegras;
    private final IDiretorRepositorio diretorRepo;
    private final IGerenteRepositorio gerenteRepo;
    private final IFuncionarioRepositorio funcRepo;

    public TelaPrincipal(IDiretorRegras diretorRegras, IGerenteRegras gerenteRegras,
                     IFuncionarioRegras funcRegras, IDiretorRepositorio diretorRepo,
                     IGerenteRepositorio gerenteRepo, IFuncionarioRepositorio funcRepo) {
        this.diretorRegras = diretorRegras;
        this.gerenteRegras = gerenteRegras;
        this.funcRegras = funcRegras;
        this.diretorRepo = diretorRepo;
        this.gerenteRepo = gerenteRepo;
        this.funcRepo = funcRepo;
        configurarJanela();
        container.add(new PainelLogin(this), "LOGIN");
        cardLayout.show(container, "LOGIN");
    }

    private void configurarJanela() {
        setTitle("Sistema de Registro de Ocorrencias - TI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        setLocationRelativeTo(null);
        setContentPane(container);
    }

    public void mostrarDiretor(String nomeDiretor) {
        trocarPainel(new PainelDiretor(this, diretorRegras, nomeDiretor), "DIRETOR");
    }

    public void mostrarGerente(Gerente gerente) {
        trocarPainel(new PainelGerente(this, gerenteRegras, gerente), "GERENTE");
    }

    public void mostrarFuncionario(Funcionario funcionario) {
        trocarPainel(new PainelFuncionario(this, funcRegras, funcionario), "FUNCIONARIO");
    }

    public void voltarLogin() {
        trocarPainel(new PainelLogin(this), "LOGIN");
    }

    private void trocarPainel(JPanel painel, String nome) {
        container.removeAll();
        container.add(painel, nome);
        cardLayout.show(container, nome);
        container.revalidate();
        container.repaint();
    }

    public IDiretorRepositorio getDiretorRepo() { return diretorRepo; }
    public IGerenteRepositorio getGerenteRepo() { return gerenteRepo; }
    public IFuncionarioRepositorio getFuncRepo() { return funcRepo; }

    // cria um botao padronizado com a cor de fundo
    public static JButton botao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // aplica estilo na tabela
    public static void estilizarTabela(JTable tabela, Color corCabecalho) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setShowGrid(true);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(COR_SELECAO);
        tabela.setSelectionForeground(COR_TEXTO);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(COR_TEXTO);
        tabela.setFillsViewportHeight(true);

        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(corCabecalho);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 32));
    }
}
