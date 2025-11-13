import java.sql.*;

/**
 * DatabaseConnection.java
 * Manages the connection to MySQL database for the Meal Planner application
 */
public class DatabaseConnection {
    // Database credentials - UPDATE THESE if needed
    private static final String DB_URL = "jdbc:mysql://localhost:3306/heal_meal_db";
    private static final String USER = "root";
    private static final String PASS = ""; // Empty by default for XAMPP
    
    private static Connection connection = null;
    
    /**
     * Get database connection
     * Creates a new connection if one doesn't exist
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.jdbc.Driver");
                
                // Establish connection
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Database connected successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-java.jar is in the lib folder");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Connection failed! Check your database settings.");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection!");
            e.printStackTrace();
        }
    }
    
    /**
     * Test the database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection test successful!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed!");
            e.printStackTrace();
        }
        return false;
    }
}