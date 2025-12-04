import java.io.*;
import java.sql.*;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class RecipeDetailHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        
        if (parts.length < 4) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid URL\"}");
            return;
        }

        String recipeIdStr = parts[3];

        if (!"GET".equals(exchange.getRequestMethod())) {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int recipeId = Integer.parseInt(recipeIdStr);
            conn = DatabaseConnection.getConnection();

            String query = "SELECT r.recipe_id, r.title, r.description, r.prep_time, r.cook_time, " +
                    "r.servings, r.difficulty, r.category, " +
                    "COALESCE(AVG(ra.rating), 0) as avg_rating, COUNT(ra.rating_id) as rating_count " +
                    "FROM recipe r " +
                    "LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id " +
                    "WHERE r.recipe_id = ? " +
                    "GROUP BY r.recipe_id, r.title, r.description, r.prep_time, r.cook_time, " +
                    "r.servings, r.difficulty, r.category";

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, recipeId);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"Recipe not found\"}");
                return;
            }

            StringBuilder json = new StringBuilder("{");
            json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
            json.append("\"title\":\"").append(WebServer.escapeJson(rs.getString("title"))).append("\",");
            json.append("\"description\":\"").append(WebServer.escapeJson(rs.getString("description"))).append("\",");
            json.append("\"prepTime\":").append(rs.getInt("prep_time")).append(",");
            json.append("\"cookTime\":").append(rs.getInt("cook_time")).append(",");
            json.append("\"servings\":").append(rs.getInt("servings")).append(",");
            json.append("\"difficulty\":\"").append(WebServer.escapeJson(rs.getString("difficulty"))).append("\",");
            json.append("\"category\":\"").append(WebServer.escapeJson(rs.getString("category"))).append("\",");
            json.append("\"rating\":").append(Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0).append(",");
            json.append("\"ratingCount\":").append(rs.getInt("rating_count")).append(",");

            rs.close();
            pstmt.close();

            String ingredientsQuery = "SELECT i.name, ri.quantity, ri.unit " +
                    "FROM recipeingredient ri " +
                    "JOIN ingredient i ON ri.ingredient_id = i.ingredient_id " +
                    "WHERE ri.recipe_id = ?";
            pstmt = conn.prepareStatement(ingredientsQuery);
            pstmt.setInt(1, recipeId);
            rs = pstmt.executeQuery();

            json.append("\"ingredients\":[");
            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"name\":\"").append(WebServer.escapeJson(rs.getString("name"))).append("\",");
                json.append("\"quantity\":").append(rs.getDouble("quantity")).append(",");
                json.append("\"unit\":\"").append(WebServer.escapeJson(rs.getString("unit"))).append("\"");
                json.append("}");
                first = false;
            }
            json.append("]");

            json.append("}");

            WebServer.sendJsonResponse(exchange, 200, json.toString());
            System.out.println("Served recipe details for ID: " + recipeId);

        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid recipe ID\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
