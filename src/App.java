import interfaces.*;
import repositories.mysql.*;
import services.*;
import ui.swing.MainFrame;
import validators.ValidacaoOcorrencia;

import javax.swing.*;

/**
 * Ponto de entrada da aplicacao com interface grafica e banco de dados MySQL.
 *
 * IoC - Inversao de Controle:
 * Os repositorios MySQL sao instanciados aqui e injetados nos servicos.
 * Para voltar a versao em memoria, basta trocar os repositorios MySQL
 * pelos repositorios em memoria (ex: new DepartamentoRepository()).
 */
public class App {

    public static void main(String[] args) {
        // --- Repositorios MySQL ---
        IDepartamentoRepository deptoRepo      = new DepartamentoRepositoryMySQL();
        IDiretorRepository      diretorRepo    = new DiretorRepositoryMySQL();
        IGerenteRepository      gerenteRepo    = new GerenteRepositoryMySQL();
        IFuncionarioRepository  funcRepo       = new FuncionarioRepositoryMySQL();
        IOcorrenciaRepository   ocorrenciaRepo = new OcorrenciaRepositoryMySQL();

        // --- Validador ---
        ValidacaoOcorrencia validacao = new ValidacaoOcorrencia();

        // --- Servicos ---
        IDiretorService     diretorService = new DiretorService(deptoRepo, gerenteRepo);
        IGerenteService     gerenteService = new GerenteService(funcRepo, deptoRepo, ocorrenciaRepo, validacao);
        IFuncionarioService funcService    = new FuncionarioService(ocorrenciaRepo);

        // --- Interface Grafica ---
        SwingUtilities.invokeLater(() -> {
            // Usar Nimbus LAF para controle total das cores
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
                try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
                catch (Exception ignored2) {}
            }

            // Forcar cores escuras nos textos para garantir legibilidade
            javax.swing.plaf.ColorUIResource textoEscuro = new javax.swing.plaf.ColorUIResource(20, 20, 20);
            javax.swing.plaf.ColorUIResource fundoClaro  = new javax.swing.plaf.ColorUIResource(255, 255, 255);
            javax.swing.plaf.ColorUIResource fundoPainel = new javax.swing.plaf.ColorUIResource(245, 247, 250);
            UIManager.put("Label.foreground", textoEscuro);
            UIManager.put("TextField.foreground", textoEscuro);
            UIManager.put("TextField.background", fundoClaro);
            UIManager.put("TextArea.foreground", textoEscuro);
            UIManager.put("ComboBox.foreground", textoEscuro);
            UIManager.put("Table.foreground", textoEscuro);
            UIManager.put("Table.background", fundoClaro);
            UIManager.put("TableHeader.foreground", new javax.swing.plaf.ColorUIResource(255, 255, 255));
            UIManager.put("TabbedPane.foreground", textoEscuro);
            UIManager.put("List.foreground", textoEscuro);
            UIManager.put("Panel.background", fundoPainel);
            UIManager.put("OptionPane.messageForeground", textoEscuro);
            UIManager.put("nimbusBase", new javax.swing.plaf.ColorUIResource(30, 60, 120));
            UIManager.put("control", fundoPainel);
            UIManager.put("text", textoEscuro);

            MainFrame janela = new MainFrame(
                diretorService, gerenteService, funcService,
                diretorRepo, gerenteRepo, funcRepo
            );
            janela.setVisible(true);
        });
    }
}
