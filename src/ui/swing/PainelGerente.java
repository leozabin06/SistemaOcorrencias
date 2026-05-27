package ui.swing;

import entidades.Departamento;
import entidades.Funcionario;
import entidades.Gerente;
import entidades.Ocorrencia;
import enums.StatusEntidade;
import enums.StatusOcorrencia;
import erros.ErroValidacao;
import interfaces.IGerenteRegras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PainelGerente extends JPanel {

    private static final DateTimeFormatter FMT   = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color             COR_G = new Color(0, 90, 120);

    private final TelaPrincipal      frame;
    private final IGerenteRegras regras;
    private final Gerente         gerente;

    private DefaultTableModel modelFunc;
    private DefaultTableModel modelOcorr;

    public PainelGerente(TelaPrincipal frame, IGerenteRegras regras, Gerente gerente) {
        this.frame   = frame;
        this.regras = regras;
        this.gerente = gerente;
        setLayout(new BorderLayout());
        setBackground(TelaPrincipal.COR_FUNDO);
        add(cabecalho(), BorderLayout.NORTH);
        add(corpo(),     BorderLayout.CENTER);
    }

    private JPanel cabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COR_G);
        p.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("GERENTE  —  " + gerente.getNome()
                + "    |    Depto: " + gerente.getDepartamento().getNome());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(Color.WHITE);

        JButton sair = TelaPrincipal.botao("Sair", TelaPrincipal.COR_BOTAO_VERM);
        sair.addActionListener(e -> frame.voltarLogin());
        p.add(titulo, BorderLayout.WEST);
        p.add(sair,   BorderLayout.EAST);
        return p;
    }

    private JTabbedPane corpo() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setForeground(TelaPrincipal.COR_TEXTO);
        tabs.addTab("  Funcionarios  ", abaFunc());
        tabs.addTab("  Ocorrencias  ",  abaOcorr());
        return tabs;
    }

    // aba funcionarios

    private JPanel abaFunc() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));

        modelFunc = new DefaultTableModel(new String[]{"Matricula","Nome","Departamento","Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelFunc);
        TelaPrincipal.estilizarTabela(tabela, COR_G);
        carregarFunc();

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barra.setBackground(TelaPrincipal.COR_FUNDO);
        JButton btnNovo    = TelaPrincipal.botao("+ Novo Funcionario",  TelaPrincipal.COR_BOTAO_AZUL);
        JButton btnEditar  = TelaPrincipal.botao("Editar Selecionado",  TelaPrincipal.COR_BOTAO_VERDE);
        JButton btnRefresh = TelaPrincipal.botao("Atualizar",           TelaPrincipal.COR_BOTAO_CINZA);
        btnNovo.addActionListener(e    -> novoFunc());
        btnEditar.addActionListener(e  -> editarFunc(tabela));
        btnRefresh.addActionListener(e -> carregarFunc());
        barra.add(btnNovo); barra.add(btnEditar); barra.add(btnRefresh);

        p.add(barra, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private void carregarFunc() {
        modelFunc.setRowCount(0);
        try {
            for (Funcionario f : regras.listarFuncionarios())
                modelFunc.addRow(new Object[]{f.getMatricula(), f.getNome(),
                        f.getDepartamento().getNome(), f.getStatus().getValor()});
        } catch (Exception ex) { erro("Erro ao carregar funcionarios: " + ex.getMessage()); }
    }

    private void novoFunc() {
        List<Departamento> deptos = deptos();
        if (deptos == null || deptos.isEmpty()) { erro("Nenhum departamento cadastrado."); return; }
        JTextField fMat  = new JTextField();
        JTextField fNome = new JTextField();
        JComboBox<Departamento> cbDepto = comboDepto(deptos);
        JComboBox<StatusEntidade> cbStatus = new JComboBox<>(StatusEntidade.values());
        Object[] campos = {"Matricula:", fMat, "Nome:", fNome, "Departamento:", cbDepto, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Novo Funcionario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            Departamento dep = (Departamento) cbDepto.getSelectedItem();
            regras.cadastrarFuncionario(fMat.getText().trim(), fNome.getText().trim(),
                    dep.getCodigo(), (StatusEntidade) cbStatus.getSelectedItem());
            carregarFunc();
            sucesso("Funcionario cadastrado!");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private void editarFunc(JTable t) {
        int row = t.getSelectedRow();
        if (row < 0) { erro("Selecione um funcionario."); return; }
        String mat    = (String) modelFunc.getValueAt(row, 0);
        String nome   = (String) modelFunc.getValueAt(row, 1);
        String status = (String) modelFunc.getValueAt(row, 3);
        List<Departamento> deptos = deptos();
        if (deptos == null) return;
        JTextField fNome = new JTextField(nome);
        JComboBox<Departamento> cbDepto = comboDepto(deptos);
        JComboBox<StatusEntidade> cbStatus = new JComboBox<>(StatusEntidade.values());
        cbStatus.setSelectedItem(StatusEntidade.fromValor(status));
        Object[] campos = {"Nome:", fNome, "Departamento:", cbDepto, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Editar Funcionario [" + mat + "]",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            Departamento dep = (Departamento) cbDepto.getSelectedItem();
            regras.alterarFuncionario(mat, fNome.getText().trim(), dep.getCodigo(),
                    (StatusEntidade) cbStatus.getSelectedItem());
            carregarFunc();
            sucesso("Funcionario atualizado!");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    // aba ocorrencias

    private JPanel abaOcorr() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));

        modelOcorr = new DefaultTableModel(
                new String[]{"#","Descricao","Data Ocorr.","Funcionario","Data Limite",
                             "Status Temp.","Status Def."}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelOcorr);
        TelaPrincipal.estilizarTabela(tabela, COR_G);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(240);
        carregarOcorr();

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barra.setBackground(TelaPrincipal.COR_FUNDO);
        JButton btnReg    = TelaPrincipal.botao("+ Registrar",        TelaPrincipal.COR_BOTAO_AZUL);
        JButton btnAlt    = TelaPrincipal.botao("Alterar",            TelaPrincipal.COR_BOTAO_VERDE);
        JButton btnEnc    = TelaPrincipal.botao("Alterar Status Def.",new Color(140, 80, 0));
        JButton btnRefresh= TelaPrincipal.botao("Atualizar",          TelaPrincipal.COR_BOTAO_CINZA);
        btnReg.addActionListener(e     -> registrar());
        btnAlt.addActionListener(e     -> alterar(tabela));
        btnEnc.addActionListener(e     -> alterarStatusDef(tabela));
        btnRefresh.addActionListener(e -> carregarOcorr());
        barra.add(btnReg); barra.add(btnAlt); barra.add(btnEnc); barra.add(btnRefresh);

        p.add(barra, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private void carregarOcorr() {
        modelOcorr.setRowCount(0);
        try {
            for (Ocorrencia o : regras.listarOcorrenciasPorDepto(gerente.getDepartamento().getCodigo()))
                modelOcorr.addRow(new Object[]{
                    o.getNumero(), o.getDescricao(),
                    o.getDataOcorrencia().format(FMT),
                    o.getFuncionarioAlocado().getNome(),
                    o.getDataLimite().format(FMT),
                    o.getStatusTemporario().getValor(),
                    o.getStatusDefinitivo().getValor()
                });
        } catch (Exception ex) { erro("Erro ao carregar ocorrencias: " + ex.getMessage()); }
    }

    private void registrar() {
        List<Funcionario> funcInfo;
        try { funcInfo = regras.listarFuncionariosInformatica(); }
        catch (Exception ex) { erro("Erro: " + ex.getMessage()); return; }
        if (funcInfo.isEmpty()) {
            erro("Nenhum funcionario do depto de Informatica encontrado.\n\n"
               + "Para registrar ocorrencias, e necessario:\n"
               + "1. Cadastrar um departamento com nome contendo 'Informatica' ou 'TI'\n"
               + "2. Cadastrar um funcionario vinculado a esse departamento");
            return;
        }
        JTextField fDesc      = new JTextField();
        JTextField fDataOcorr = new JTextField(LocalDate.now().format(FMT));
        JTextField fDataLim   = new JTextField();
        JComboBox<Funcionario> cbFunc = comboFunc(funcInfo);

        Object[] campos = {
            "Descricao:",                    fDesc,
            "Data Ocorrencia (dd/MM/yyyy):", fDataOcorr,
            "Data Limite (dd/MM/yyyy):",     fDataLim,
            "Funcionario (Informatica):",    cbFunc
        };
        if (JOptionPane.showConfirmDialog(frame, campos, "Registrar Ocorrencia",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            regras.registrarOcorrencia(gerente, fDesc.getText().trim(),
                    LocalDate.parse(fDataOcorr.getText().trim(), FMT),
                    LocalDate.parse(fDataLim.getText().trim(), FMT),
                    ((Funcionario) cbFunc.getSelectedItem()).getMatricula());
            carregarOcorr();
            sucesso("Ocorrencia registrada!");
        } catch (DateTimeParseException ex) {
            erro("Formato de data invalido. Use dd/MM/yyyy.");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private void alterar(JTable t) {
        int row = t.getSelectedRow();
        if (row < 0) { erro("Selecione uma ocorrencia."); return; }
        int    num  = (int)    modelOcorr.getValueAt(row, 0);
        String desc = (String) modelOcorr.getValueAt(row, 1);
        String dLim = (String) modelOcorr.getValueAt(row, 4);

        String[] opcoes = {"Descricao", "Data Limite", "Funcionario Alocado"};
        String escolha = (String) JOptionPane.showInputDialog(frame, "O que deseja alterar?",
                "Alterar Ocorrencia #" + num, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;
        try {
            switch (escolha) {
                case "Descricao" -> {
                    JTextField f = new JTextField(desc);
                    if (JOptionPane.showConfirmDialog(frame, new Object[]{"Nova descricao:", f},
                            "Alterar", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
                    regras.alterarOcorrencia(num, gerente, f.getText().trim(), null, null);
                }
                case "Data Limite" -> {
                    JTextField f = new JTextField(dLim);
                    if (JOptionPane.showConfirmDialog(frame, new Object[]{"Nova data limite (dd/MM/yyyy):", f},
                            "Alterar", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
                    regras.alterarOcorrencia(num, gerente, null, LocalDate.parse(f.getText().trim(), FMT), null);
                }
                default -> {
                    List<Funcionario> funcInfo;
                    try { funcInfo = regras.listarFuncionariosInformatica(); }
                    catch (Exception ex) { erro("Erro: " + ex.getMessage()); return; }
                    if (funcInfo.isEmpty()) { erro("Nenhum funcionario de Informatica."); return; }
                    JComboBox<Funcionario> cb = comboFunc(funcInfo);
                    if (JOptionPane.showConfirmDialog(frame, new Object[]{"Novo funcionario:", cb},
                            "Alterar", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
                    regras.alterarOcorrencia(num, gerente, null, null,
                            ((Funcionario) cb.getSelectedItem()).getMatricula());
                }
            }
            carregarOcorr();
            sucesso("Ocorrencia atualizada!");
        } catch (DateTimeParseException ex) {
            erro("Formato de data invalido. Use dd/MM/yyyy.");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private void alterarStatusDef(JTable t) {
        int row = t.getSelectedRow();
        if (row < 0) { erro("Selecione uma ocorrencia."); return; }
        int num = (int) modelOcorr.getValueAt(row, 0);
        String[] opcoes = {"aberta", "encerrada"};
        String escolha = (String) JOptionPane.showInputDialog(frame,
                "Novo status definitivo para ocorrencia #" + num + ":",
                "Status Definitivo", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;
        try {
            regras.alterarStatusDefinitivo(num, gerente,
                    "encerrada".equals(escolha) ? StatusOcorrencia.ENCERRADA : StatusOcorrencia.ABERTA);
            carregarOcorr();
            sucesso("Status definitivo: " + escolha);
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private List<Departamento> deptos() {
        try { return regras.listarDepartamentos(); }
        catch (Exception ex) { erro("Erro ao buscar departamentos: " + ex.getMessage()); return null; }
    }

    private JComboBox<Departamento> comboDepto(List<Departamento> lista) {
        JComboBox<Departamento> cb = new JComboBox<>(lista.toArray(new Departamento[0]));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Departamento d) setText("[" + d.getCodigo() + "] " + d.getNome());
                return this;
            }
        });
        return cb;
    }

    private JComboBox<Funcionario> comboFunc(List<Funcionario> lista) {
        JComboBox<Funcionario> cb = new JComboBox<>(lista.toArray(new Funcionario[0]));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Funcionario fn) setText(fn.getNome() + " [" + fn.getMatricula() + "]");
                return this;
            }
        });
        return cb;
    }

    private void sucesso(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    private void erro(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
