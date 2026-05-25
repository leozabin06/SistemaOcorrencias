package ui.swing;

import entidades.Departamento;
import entidades.Gerente;
import erros.ErroValidacao;
import interfaces.IDiretorRegras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelDiretor extends JPanel {

    private final TelaPrincipal      frame;
    private final IDiretorRegras service;
    private final String          nomeDiretor;

    private DefaultTableModel modelDepto;
    private DefaultTableModel modelGerente;

    public PainelDiretor(TelaPrincipal frame, IDiretorRegras service, String nomeDiretor) {
        this.frame       = frame;
        this.service     = service;
        this.nomeDiretor = nomeDiretor;
        setLayout(new BorderLayout());
        setBackground(TelaPrincipal.COR_FUNDO);
        add(cabecalho(), BorderLayout.NORTH);
        add(corpo(),     BorderLayout.CENTER);
    }

    private JPanel cabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(TelaPrincipal.COR_CABECALHO);
        p.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("DIRETOR  —  " + nomeDiretor);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
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
        tabs.setBackground(TelaPrincipal.COR_FUNDO);
        tabs.setForeground(TelaPrincipal.COR_TEXTO);
        tabs.addTab("  Departamentos  ", abaDepto());
        tabs.addTab("  Gerentes  ",      abaGerente());
        return tabs;
    }

    // aba departamentos

    private JPanel abaDepto() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));

        String[] cols = {"Codigo", "Nome", "Descricao", "Status"};
        modelDepto = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelDepto);
        TelaPrincipal.estilizarTabela(tabela, TelaPrincipal.COR_CABECALHO);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
        carregarDeptos();

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barra.setBackground(TelaPrincipal.COR_FUNDO);
        JButton btnNovo    = TelaPrincipal.botao("+ Novo Departamento", TelaPrincipal.COR_BOTAO_AZUL);
        JButton btnEditar  = TelaPrincipal.botao("Editar Selecionado",  TelaPrincipal.COR_BOTAO_VERDE);
        JButton btnRefresh = TelaPrincipal.botao("Atualizar",           TelaPrincipal.COR_BOTAO_CINZA);
        btnNovo.addActionListener(e    -> novoDepto());
        btnEditar.addActionListener(e  -> editarDepto(tabela));
        btnRefresh.addActionListener(e -> carregarDeptos());
        barra.add(btnNovo); barra.add(btnEditar); barra.add(btnRefresh);

        p.add(barra, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private void carregarDeptos() {
        modelDepto.setRowCount(0);
        try {
            for (Departamento d : service.listarDepartamentos())
                modelDepto.addRow(new Object[]{d.getCodigo(), d.getNome(), d.getDescricao(), d.getStatus()});
        } catch (Exception ex) { erro("Erro ao carregar departamentos:\n" + ex.getMessage()); }
    }

    private void novoDepto() {
        JTextField fCod  = new JTextField();
        JTextField fNome = new JTextField();
        JTextField fDesc = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ativo", "inativo"});
        Object[] campos = {"Codigo:", fCod, "Nome:", fNome, "Descricao:", fDesc, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Novo Departamento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            service.cadastrarDepartamento(Integer.parseInt(fCod.getText().trim()),
                    fNome.getText().trim(), fDesc.getText().trim(), (String) cbStatus.getSelectedItem());
            carregarDeptos();
            sucesso("Departamento cadastrado!");
        } catch (NumberFormatException ex) {
            erro("Codigo deve ser um numero.");
        } catch (ErroValidacao ex) {
            erro(ex.getMessage());
        } catch (Exception ex) {
            erro("Erro: " + ex.getMessage());
        }
    }

    private void editarDepto(JTable t) {
        int row = t.getSelectedRow();
        if (row < 0) { erro("Selecione um departamento na tabela."); return; }
        int    cod    = (int)    modelDepto.getValueAt(row, 0);
        String nome   = (String) modelDepto.getValueAt(row, 1);
        String desc   = (String) modelDepto.getValueAt(row, 2);
        String status = (String) modelDepto.getValueAt(row, 3);

        JTextField fNome = new JTextField(nome);
        JTextField fDesc = new JTextField(desc);
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ativo", "inativo"});
        cbStatus.setSelectedItem(status);
        Object[] campos = {"Nome:", fNome, "Descricao:", fDesc, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Editar Departamento [" + cod + "]",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            service.alterarDepartamento(cod, fNome.getText().trim(), fDesc.getText().trim(),
                    (String) cbStatus.getSelectedItem());
            carregarDeptos();
            sucesso("Departamento atualizado!");
        } catch (ErroValidacao | RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    // aba gerentes

    private JPanel abaGerente() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(TelaPrincipal.COR_FUNDO);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));

        String[] cols = {"Matricula", "Nome", "Departamento", "Status"};
        modelGerente = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelGerente);
        TelaPrincipal.estilizarTabela(tabela, TelaPrincipal.COR_CABECALHO);
        carregarGerentes();

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barra.setBackground(TelaPrincipal.COR_FUNDO);
        JButton btnNovo    = TelaPrincipal.botao("+ Novo Gerente",    TelaPrincipal.COR_BOTAO_AZUL);
        JButton btnEditar  = TelaPrincipal.botao("Editar Selecionado", TelaPrincipal.COR_BOTAO_VERDE);
        JButton btnRefresh = TelaPrincipal.botao("Atualizar",          TelaPrincipal.COR_BOTAO_CINZA);
        btnNovo.addActionListener(e    -> novoGerente());
        btnEditar.addActionListener(e  -> editarGerente(tabela));
        btnRefresh.addActionListener(e -> carregarGerentes());
        barra.add(btnNovo); barra.add(btnEditar); barra.add(btnRefresh);

        p.add(barra, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private void carregarGerentes() {
        modelGerente.setRowCount(0);
        try {
            for (Gerente g : service.listarGerentes())
                modelGerente.addRow(new Object[]{g.getMatricula(), g.getNome(),
                        g.getDepartamento().getNome(), g.getStatus()});
        } catch (Exception ex) { erro("Erro ao carregar gerentes:\n" + ex.getMessage()); }
    }

    private void novoGerente() {
        List<Departamento> deptos = buscarDeptos();
        if (deptos == null || deptos.isEmpty()) { erro("Cadastre ao menos um departamento primeiro."); return; }
        JTextField fMat  = new JTextField();
        JTextField fNome = new JTextField();
        JComboBox<Departamento> cbDepto = comboDepto(deptos);
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ativo", "inativo"});
        Object[] campos = {"Matricula:", fMat, "Nome:", fNome, "Departamento:", cbDepto, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Novo Gerente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            Departamento dep = (Departamento) cbDepto.getSelectedItem();
            service.cadastrarGerente(fMat.getText().trim(), fNome.getText().trim(),
                    dep.getCodigo(), (String) cbStatus.getSelectedItem());
            carregarGerentes();
            sucesso("Gerente cadastrado!");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private void editarGerente(JTable t) {
        int row = t.getSelectedRow();
        if (row < 0) { erro("Selecione um gerente na tabela."); return; }
        String mat    = (String) modelGerente.getValueAt(row, 0);
        String nome   = (String) modelGerente.getValueAt(row, 1);
        String status = (String) modelGerente.getValueAt(row, 3);

        List<Departamento> deptos = buscarDeptos();
        if (deptos == null) return;
        JTextField fNome = new JTextField(nome);
        JComboBox<Departamento> cbDepto = comboDepto(deptos);
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ativo", "inativo"});
        cbStatus.setSelectedItem(status);
        Object[] campos = {"Nome:", fNome, "Departamento:", cbDepto, "Status:", cbStatus};
        if (JOptionPane.showConfirmDialog(frame, campos, "Editar Gerente [" + mat + "]",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            Departamento dep = (Departamento) cbDepto.getSelectedItem();
            service.alterarGerente(mat, fNome.getText().trim(), dep.getCodigo(),
                    (String) cbStatus.getSelectedItem());
            carregarGerentes();
            sucesso("Gerente atualizado!");
        } catch (ErroValidacao | RuntimeException ex) { erro(ex.getMessage()); }
    }

    private List<Departamento> buscarDeptos() {
        try { return service.listarDepartamentos(); }
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

    private void sucesso(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    private void erro(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
