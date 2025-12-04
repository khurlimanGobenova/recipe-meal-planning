import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class ShoppingListHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        String requestMethod = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if ("GET".equalsIgnoreCase(requestMethod)) {
            handleGet(exchange);
        } else {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        
        if (query == null || !query.startsWith("mealPlanId=")) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"mealPlanId parameter required\"}");
            return;
        }

        String mealPlanIdStr = query.substring(11);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int mealPlanId = Integer.parseInt(mealPlanIdStr);
            conn = DatabaseConnection.getConnection();

            String sqlQuery = "SELECT i.ingredient_id, i.name, i.unit, " +
                    "SUM(ri.quantity) as total_quantity " +
                    "FROM mealentry me " +
                    "JOIN recipeingredient ri ON me.recipe_id = ri.recipe_id " +
                    "JOIN ingredient i ON ri.ingredient_id = i.ingredient_id " +
                    "WHERE me.mealplan_id = ? " +
                    "GROUP BY i.ingredient_id, i.name, i.unit " +
                    "ORDER BY i.name";

            pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, mealPlanId);
            rs = pstmt.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("ingredient_id")).append(",");
                json.append("\"name\":\"").append(WebServer.escapeJson(rs.getString("name"))).append("\",");
                json.append("\"quantity\":").append(Math.round(rs.getDouble("total_quantity") * 100.0) / 100.0).append(",");
                json.append("\"unit\":\"").append(WebServer.escapeJson(rs.getString("unit"))).append("\"");
                json.append("}");
                first = false;
            }

            json.append("]");
            WebServer.sendJsonResponse(exchange, 200, json.toString());

        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid meal plan ID\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
