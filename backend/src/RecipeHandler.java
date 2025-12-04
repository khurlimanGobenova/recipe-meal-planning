import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class RecipeHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if (!"GET".equals(exchange.getRequestMethod())) {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT r.recipe_id, r.title, r.category, r.difficulty, " +
                    "(r.prep_time + r.cook_time) as total_time, " +
                    "COALESCE(AVG(ra.rating), 0) as avg_rating " +
                    "FROM recipe r " +
                    "LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id " +
                    "GROUP BY r.recipe_id, r.title, r.category, r.difficulty, r.prep_time, r.cook_time " +
                    "ORDER BY r.title " +
                    "LIMIT 50";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                json.append("\"title\":\"").append(WebServer.escapeJson(rs.getString("title"))).append("\",");
                json.append("\"category\":\"").append(WebServer.escapeJson(rs.getString("category"))).append("\",");
                json.append("\"difficulty\":\"").append(WebServer.escapeJson(rs.getString("difficulty"))).append("\",");
                json.append("\"totalTime\":").append(rs.getInt("total_time")).append(",");
                json.append("\"rating\":").append(Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0);
                json.append("}");
                first = false;
            }

            json.append("]");

            WebServer.sendJsonResponse(exchange, 200, json.toString());
            System.out.println("Served recipes list");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error: " + WebServer.escapeJson(e.getMessage()) + "\"}");
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
}
