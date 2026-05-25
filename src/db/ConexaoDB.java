package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Conexao com o MySQL - alterar usuario/senha conforme necessario
public class ConexaoDB {

    private static final String URL = "jdbc:mysql://localhost:3306/ocorrencias_ti?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
