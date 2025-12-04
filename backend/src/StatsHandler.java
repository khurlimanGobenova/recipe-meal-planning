import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class StatsHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            StringBuilder json = new StringBuilder("{");

            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM recipe");
            if (rs.next()) {
                json.append("\"totalRecipes\":").append(rs.getInt("count")).append(",");
            }

            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM ingredient");
            if (rs.next()) {
                json.append("\"totalIngredients\":").append(rs.getInt("count")).append(",");
            }

            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM user");
            if (rs.next()) {
                json.append("\"totalUsers\":").append(rs.getInt("count")).append(",");
            }

            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM mealplan");
            if (rs.next()) {
                json.append("\"totalMealPlans\":").append(rs.getInt("count"));
            }

            json.append("}");

            WebServer.sendJsonResponse(exchange, 200, json.toString());

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
}