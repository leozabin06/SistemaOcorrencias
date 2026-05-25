package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitario de conexao com o banco de dados MySQL.
 * Altere USER e PASSWORD conforme sua instalacao local.
 */
public class ConexaoDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/ocorrencias_ti?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456";   // <- coloque sua senha do MySQL aqui

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
