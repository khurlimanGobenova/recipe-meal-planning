import java.sql.*;

public class DatabaseConnection {
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/heal_meal_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";  // Default XAMPP password is empty
    
    // JDBC driver name
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    
    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Open connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            throw new SQLException("Driver not found", e);
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful!");
                System.out.println("  Database: " + conn.getCatalog());
                System.out.println("  URL: " + DB_URL);
                
                conn.close();
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("  Error: " + e.getMessage());
            System.err.println("\nTroubleshooting:");
            System.err.println("  1. Is XAMPP MySQL running?");
            System.err.println("  2. Does database 'heal_meal_db' exist?");
            System.err.println("  3. Have you run schema.sql and data.sql?");
            return false;
        }
    }
    
    /**
     * Close database resources safely
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) rs.close();
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }
    
    /**
     * Close connection and statement
     */
    public static void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }
    
    /**
     * Close connection only
     */
    public static void closeResources(Connection conn) {
        closeResources(conn, null, null);
    }
    
    /**
     * Execute a query and return result count
     * Useful for testing
     */
    public static int getTableCount(String tableName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + tableName);
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
            return 0;
            
        } catch (SQLException e) {
            System.err.println("Error counting records in " + tableName + ": " + e.getMessage());
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    /**
     * Main method for testing connection
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   DATABASE CONNECTION TEST                     ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        if (testConnection()) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║   DATABASE STATISTICS                          ║");
            System.out.println("╠════════════════════════════════════════════════╣");
            
            String[] tables = {"user", "recipe", "ingredient", "mealplan", 
                             "recipeingredient", "rating", "mealentry"};
            
            for (String table : tables) {
                int count = getTableCount(table);
                if (count >= 0) {
                    System.out.printf("║ %-20s : %5d records          ║%n", 
                                    table.toUpperCase(), count);
                }
            }
            
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("\n✓ Database is ready for web application!");
            
        } else {
            System.out.println("\n✗ Database connection failed!");
            System.out.println("Please fix the issues above before starting the web server.");
            System.exit(1);
        }
    }
}