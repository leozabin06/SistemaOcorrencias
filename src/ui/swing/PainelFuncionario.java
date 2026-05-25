package ui.swing;

import entidades.Funcionario;
import entidades.Ocorrencia;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import interfaces.IFuncionarioRegras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PainelFuncionario extends JPanel {

    private static final DateTimeFormatter FMT   = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color             COR_F = new Color(40, 100, 50);

    private final TelaPrincipal         frame;
    private final IFuncionarioRegras regras;
    private final Funcionario         funcionario;

    private DefaultTableModel modelOcorr;
    private JTable            tabela;

    public PainelFuncionario(TelaPrincipal frame, IFuncionarioRegras regras, Funcionario funcionario) {
        this.frame       = frame;
        this.regras = regras;
        this.funcionario = funcionario;
        setLayout(new BorderLayout());
        setBackground(TelaPrincipal.COR_FUNDO);
        add(cabecalho(), BorderLayout.NORTH);
        add(corpo(),     BorderLayout.CENTER);
        add(barra(),     BorderLayout.SOUTH);
    }

    private JPanel cabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COR_F);
        p.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("FUNCIONARIO  —  " + funcionario.getNome()
                + "    |    Depto: " + funcionario.getDepartamento().getNome()
                + "    |    Mat: " + funcionario.getMatricula());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);

        JButton sair = TelaPrincipal.botao("Sair", TelaPrincipal.COR_BOTAO_VERM);
        sair.addActionListener(e -> frame.voltarLogin());
        p.add(titulo, BorderLayout.WEST);
        p.add(sair,   BorderLayout.EAST);
        return p;
    }

    private JPanel corpo() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(new EmptyBorder(14, 18, 6, 18));

        JLabel lblTitulo = new JLabel("Minhas Ocorrencias Atribuidas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(TelaPrincipal.COR_TEXTO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        modelOcorr = new DefaultTableModel(
                new String[]{"#","Descricao","Data Ocorr.","Depto Reportante",
                             "Data Limite","Status Temp.","Status Def."}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelOcorr);
        TelaPrincipal.estilizarTabela(tabela, COR_F);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
        carregarOcorr();

        p.add(lblTitulo, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private JPanel barra() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, TelaPrincipal.COR_BORDA),
            new EmptyBorder(4, 8, 4, 8)
        ));

        JButton btnStatus  = TelaPrincipal.botao("Alterar Meu Status Temporario", COR_F);
        JButton btnRefresh = TelaPrincipal.botao("Atualizar Lista", TelaPrincipal.COR_BOTAO_CINZA);
        btnStatus.addActionListener(e  -> alterarStatus());
        btnRefresh.addActionListener(e -> carregarOcorr());

        JLabel aviso = new JLabel("  Voce pode alterar apenas o status temporario das suas ocorrencias.");
        aviso.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        aviso.setForeground(new Color(90, 90, 110));

        p.add(btnStatus);
        p.add(btnRefresh);
        p.add(aviso);
        return p;
    }

    private void carregarOcorr() {
        modelOcorr.setRowCount(0);
        try {
            for (Ocorrencia o : regras.listarMinhasOcorrencias(funcionario.getMatricula()))
                modelOcorr.addRow(new Object[]{
                    o.getNumero(), o.getDescricao(),
                    o.getDataOcorrencia().format(FMT),
                    o.getDeptoReportante().getNome(),
                    o.getDataLimite().format(FMT),
                    o.getStatusTemporario().getValor(),
                    o.getStatusDefinitivo().getValor()
                });
        } catch (Exception ex) { erro("Erro ao carregar ocorrencias: " + ex.getMessage()); }
    }

    private void alterarStatus() {
        int row = tabela.getSelectedRow();
        int num;
        if (row >= 0) {
            num = (int) modelOcorr.getValueAt(row, 0);
        } else {
            String s = JOptionPane.showInputDialog(frame,
                    "Informe o numero da ocorrencia:", "Alterar Status", JOptionPane.QUESTION_MESSAGE);
            if (s == null || s.trim().isEmpty()) return;
            try { num = Integer.parseInt(s.trim()); }
            catch (NumberFormatException ex) { erro("Numero invalido."); return; }
        }

        String[] opcoes = {"aberta", "encerrada"};
        String escolha = (String) JOptionPane.showInputDialog(frame,
                "Novo status temporario para ocorrencia #" + num + ":",
                "Status Temporario", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        try {
            regras.alterarStatusTemporario(num, funcionario.getMatricula(),
                    "encerrada".equals(escolha) ? StatusOcorrencia.ENCERRADA : StatusOcorrencia.ABERTA);
            carregarOcorr();
            JOptionPane.showMessageDialog(frame,
                    "Status temporario atualizado para: " + escolha
                    + "\n(O status definitivo e responsabilidade do gerente.)",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
