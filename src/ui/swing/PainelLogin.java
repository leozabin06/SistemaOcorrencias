package ui.swing;

import entidades.Diretor;
import entidades.Funcionario;
import entidades.Gerente;
import enums.StatusEntidade;
import interfaces.IDiretorRepositorio;
import interfaces.IFuncionarioRepositorio;
import interfaces.IGerenteRepositorio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Tela de login - usuario informa matricula e escolhe o perfil
public class PainelLogin extends JPanel {

    private final TelaPrincipal frame;
    private final JTextField campMatricula = new JTextField(20);

    public PainelLogin(TelaPrincipal frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(TelaPrincipal.COR_FUNDO);
        add(construirCabecalho(), BorderLayout.NORTH);
        add(construirCentro(),    BorderLayout.CENTER);
        add(construirRodape(),    BorderLayout.SOUTH);
    }

    private JPanel construirCabecalho() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 6));
        p.setBackground(TelaPrincipal.COR_CABECALHO);
        p.setBorder(new EmptyBorder(28, 40, 28, 40));

        JLabel titulo = new JLabel("Sistema de Registro de Ocorrencias - TI", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Faca login para continuar", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(190, 215, 255));

        p.add(titulo);
        p.add(sub);
        return p;
    }

    private JPanel construirCentro() {
        JPanel externo = new JPanel(new GridBagLayout());
        externo.setBackground(TelaPrincipal.COR_FUNDO);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TelaPrincipal.COR_BORDA, 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        card.setMaximumSize(new Dimension(420, 500));

        JLabel lblMat = new JLabel("Matricula:");
        lblMat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMat.setForeground(TelaPrincipal.COR_TEXTO);
        lblMat.setAlignmentX(Component.LEFT_ALIGNMENT);

        campMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campMatricula.setForeground(TelaPrincipal.COR_TEXTO);
        campMatricula.setBackground(Color.WHITE);
        campMatricula.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TelaPrincipal.COR_BORDA, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        campMatricula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campMatricula.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAcesso = new JLabel("Entrar como:");
        lblAcesso.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAcesso.setForeground(new Color(90, 90, 110));
        lblAcesso.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDiretor = TelaPrincipal.botao("Entrar como Diretor", TelaPrincipal.COR_BOTAO_AZUL);
        JButton btnGerente = TelaPrincipal.botao("Entrar como Gerente", new Color(0, 110, 140));
        JButton btnFuncionario = TelaPrincipal.botao("Entrar como Funcionario", TelaPrincipal.COR_BOTAO_VERDE);

        for (JButton btn : new JButton[]{btnDiretor, btnGerente, btnFuncionario}) {
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JSeparator sep = new JSeparator();
        sep.setForeground(TelaPrincipal.COR_BORDA);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        JButton btnCadDir = TelaPrincipal.botao("+ Cadastrar Novo Diretor", new Color(110, 70, 0));
        btnCadDir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCadDir.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCadDir.setBackground(new Color(140, 90, 20));

        btnDiretor.addActionListener(e -> loginDiretor());
        btnGerente.addActionListener(e -> loginGerente());
        btnFuncionario.addActionListener(e -> loginFuncionario());
        btnCadDir.addActionListener(e -> cadastrarDiretor());
        card.add(lblMat);
        card.add(Box.createVerticalStrut(6));
        card.add(campMatricula);
        card.add(Box.createVerticalStrut(20));
        card.add(lblAcesso);
        card.add(Box.createVerticalStrut(8));
        card.add(btnDiretor);
        card.add(Box.createVerticalStrut(6));
        card.add(btnGerente);
        card.add(Box.createVerticalStrut(6));
        card.add(btnFuncionario);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(btnCadDir);

        externo.add(card);
        return externo;
    }

    private JPanel construirRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(new Color(220, 225, 235));
        p.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel l = new JLabel("Sistema TI — Registro de Ocorrencias");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(new Color(80, 80, 100));
        p.add(l);
        return p;
    }

    private void loginDiretor() {
        String mat = campMatricula.getText().trim();
        if (mat.isEmpty()) { erro("Informe a matricula."); return; }
        try {
            Diretor d = frame.getDiretorRepo().buscarPorMatricula(mat);
            if (d == null) { erro("Diretor nao encontrado: " + mat); return; }
            frame.mostrarDiretor(d.getNome());
        } catch (Exception ex) {
            erro("Erro ao conectar com o banco:\n" + ex.getMessage()
                + "\n\nVerifique a senha em src/db/ConexaoDB.java");
        }
    }

    private void loginGerente() {
        String mat = campMatricula.getText().trim();
        if (mat.isEmpty()) { erro("Informe a matricula."); return; }
        try {
            IGerenteRepositorio repo = frame.getGerenteRepo();
            Gerente g = repo.buscarPorMatricula(mat);
            if (g == null) { erro("Gerente nao encontrado: " + mat); return; }
            frame.mostrarGerente(g);
        } catch (Exception ex) {
            erro("Erro ao conectar com o banco:\n" + ex.getMessage()
                + "\n\nVerifique a senha em src/db/ConexaoDB.java");
        }
    }

    private void loginFuncionario() {
        String mat = campMatricula.getText().trim();
        if (mat.isEmpty()) { erro("Informe a matricula."); return; }
        try {
            IFuncionarioRepositorio repo = frame.getFuncRepo();
            Funcionario f = repo.buscarPorMatricula(mat);
            if (f == null) { erro("Funcionario nao encontrado: " + mat); return; }
            frame.mostrarFuncionario(f);
        } catch (Exception ex) {
            erro("Erro ao conectar com o banco:\n" + ex.getMessage()
                + "\n\nVerifique a senha em src/db/ConexaoDB.java");
        }
    }

    private void cadastrarDiretor() {
        JTextField fMat  = new JTextField();
        JTextField fNome = new JTextField();
        JComboBox<StatusEntidade> cbStatus = new JComboBox<>(StatusEntidade.values());

        Object[] campos = {"Matricula:", fMat, "Nome:", fNome, "Status:", cbStatus};
        int res = JOptionPane.showConfirmDialog(frame, campos,
                "Cadastrar Diretor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String mat  = fMat.getText().trim();
        String nome = fNome.getText().trim();
        if (mat.isEmpty() || nome.isEmpty()) { erro("Matricula e Nome sao obrigatorios."); return; }

        try {
            IDiretorRepositorio repo = frame.getDiretorRepo();
            if (repo.existeMatricula(mat)) { erro("Matricula ja cadastrada."); return; }
            repo.salvar(new Diretor(mat, nome, (StatusEntidade) cbStatus.getSelectedItem()));
            JOptionPane.showMessageDialog(frame,
                "Diretor cadastrado! Agora informe a matricula e clique em 'Entrar como Diretor'.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            campMatricula.setText(mat);
        } catch (Exception ex) {
            erro("Erro ao salvar:\n" + ex.getMessage()
                + "\n\nVerifique a senha em src/db/ConexaoDB.java");
        }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
