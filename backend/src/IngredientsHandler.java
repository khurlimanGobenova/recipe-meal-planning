import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class IngredientsHandler implements HttpHandler {
    
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
            String query = "SELECT ingredient_id, name, unit, calories_per_unit, protein, fat, carbs " +
                    "FROM ingredient ORDER BY name LIMIT 50";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("ingredient_id")).append(",");
                json.append("\"name\":\"").append(WebServer.escapeJson(rs.getString("name"))).append("\",");
                json.append("\"unit\":\"").append(WebServer.escapeJson(rs.getString("unit"))).append("\",");
                json.append("\"calories\":").append(rs.getDouble("calories_per_unit")).append(",");
                json.append("\"protein\":").append(rs.getDouble("protein")).append(",");
                json.append("\"fat\":").append(rs.getDouble("fat")).append(",");
                json.append("\"carbs\":").append(rs.getDouble("carbs"));
                json.append("}");
                first = false;
            }

            json.append("]");

            WebServer.sendJsonResponse(exchange, 200, json.toString());

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, stmt, rs);
        }
    }
}