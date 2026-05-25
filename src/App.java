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
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainFrame janela = new MainFrame(
                diretorService, gerenteService, funcService,
                diretorRepo, gerenteRepo, funcRepo
            );
            janela.setVisible(true);
        });
    }
}
