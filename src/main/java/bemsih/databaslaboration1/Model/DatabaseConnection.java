package bemsih.databaslaboration1.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides functionality to establish a connection to the database.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Laboration1"; // URL for the database
    private static final String USER = "root"; // Username for the database
    private static final String PASSWORD = "zxcvbnm,.-"; // Password for the database

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A {@link Connection} object representing the database connection.
     * @throws SQLException If an SQL error occurs while trying to connect.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
