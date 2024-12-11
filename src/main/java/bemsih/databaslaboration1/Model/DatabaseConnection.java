package bemsih.databaslaboration1.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Laboration1";
    private static final String USER = "root";
    private static final String PASSWORD = "zxcvbnm,.-";

    // Metod för att skapa en anslutning
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}