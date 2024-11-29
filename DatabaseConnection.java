import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Define the URL, USER, and PASSWORD for the MySQL database
    private static final String URL = "jdbc:mysql://localhost:3306/bakerysystem?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // Update this with your DB username
    private static final String PASSWORD = "8752";  // Update this with your DB password

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver (newer MySQL versions use com.mysql.cj.jdbc.Driver)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Return the connection object
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // If the MySQL JDBC driver is not found, this block will be executed
            System.err.println("MySQL JDBC Driver not found. Please add the JDBC driver to your classpath.");
            e.printStackTrace();
            throw new SQLException("Error: MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            // If a connection error occurs, this block will handle it
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Error connecting to the database", e);
        }
    }
}
