import interfaces.*;
import repositorios.mysql.*;
import servicos.*;
import ui.swing.TelaPrincipal;
import validadores.ValidacaoOcorrencia;

import javax.swing.*;

// Classe principal - inicializa os repositorios e abre a tela
public class App {

    public static void main(String[] args) {
        // repositorios
        IDepartamentoRepositorio deptoRepo = new DepartamentoRepositorioMySQL();
        IDiretorRepositorio diretorRepo = new DiretorRepositorioMySQL();
        IGerenteRepositorio gerenteRepo = new GerenteRepositorioMySQL();
        IFuncionarioRepositorio funcRepo = new FuncionarioRepositorioMySQL();
        IOcorrenciaRepositorio ocorrenciaRepo = new OcorrenciaRepositorioMySQL();

        ValidacaoOcorrencia validacao = new ValidacaoOcorrencia();

        // servicos (inversao de controle - injeta os repos pelo construtor)
        IDiretorServico diretorService = new DiretorServico(deptoRepo, gerenteRepo);
        IGerenteServico gerenteService = new GerenteServico(funcRepo, deptoRepo, ocorrenciaRepo, validacao);
        IFuncionarioServico funcService = new FuncionarioServico(ocorrenciaRepo);

        // abre a interface grafica
        SwingUtilities.invokeLater(() -> {
            // tenta usar Nimbus pra ficar mais bonito
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
                catch (Exception ex) {}
            }

            // configura cores pra ficar legivel
            javax.swing.plaf.ColorUIResource corTexto = new javax.swing.plaf.ColorUIResource(20, 20, 20);
            javax.swing.plaf.ColorUIResource corFundo = new javax.swing.plaf.ColorUIResource(255, 255, 255);
            javax.swing.plaf.ColorUIResource corPainel = new javax.swing.plaf.ColorUIResource(245, 247, 250);
            UIManager.put("Label.foreground", corTexto);
            UIManager.put("TextField.foreground", corTexto);
            UIManager.put("TextField.background", corFundo);
            UIManager.put("TextArea.foreground", corTexto);
            UIManager.put("ComboBox.foreground", corTexto);
            UIManager.put("Table.foreground", corTexto);
            UIManager.put("Table.background", corFundo);
            UIManager.put("TableHeader.foreground", new javax.swing.plaf.ColorUIResource(255, 255, 255));
            UIManager.put("TabbedPane.foreground", corTexto);
            UIManager.put("List.foreground", corTexto);
            UIManager.put("Panel.background", corPainel);
            UIManager.put("OptionPane.messageForeground", corTexto);
            UIManager.put("nimbusBase", new javax.swing.plaf.ColorUIResource(30, 60, 120));
            UIManager.put("control", corPainel);
            UIManager.put("text", corTexto);

            TelaPrincipal janela = new TelaPrincipal(
                diretorService, gerenteService, funcService,
                diretorRepo, gerenteRepo, funcRepo
            );
            janela.setVisible(true);
        });
    }
}
