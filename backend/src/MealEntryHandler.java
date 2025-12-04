import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class MealEntryHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        // Check if this is a DELETE request for specific entry
        if (path.matches("/api/mealentries/\\d+")) {
            handleDelete(exchange, path);
            return;
        }

        if ("GET".equalsIgnoreCase(requestMethod)) {
            handleGet(exchange);
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            handlePost(exchange);
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

            String sqlQuery = "SELECT me.mealentry_id, me.mealplan_id, me.recipe_id, me.date, me.meal_type, " +
                    "r.title as recipe_title " +
                    "FROM mealentry me " +
                    "JOIN recipe r ON me.recipe_id = r.recipe_id " +
                    "WHERE me.mealplan_id = ? " +
                    "ORDER BY me.date, FIELD(me.meal_type, 'Breakfast', 'Lunch', 'Dinner', 'Snack')";

            pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, mealPlanId);
            rs = pstmt.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("mealentry_id")).append(",");
                json.append("\"mealPlanId\":").append(rs.getInt("mealplan_id")).append(",");
                json.append("\"recipeId\":").append(rs.getInt("recipe_id")).append(",");
                json.append("\"planDate\":\"").append(rs.getDate("date")).append("\",");
                json.append("\"mealType\":\"").append(WebServer.escapeJson(rs.getString("meal_type"))).append("\",");
                json.append("\"recipeTitle\":\"").append(WebServer.escapeJson(rs.getString("recipe_title"))).append("\"");
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

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody()))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        Map<String, String> data = WebServer.parseJson(requestBody);
        String mealPlanIdStr = data.get("mealPlanId");
        String recipeIdStr = data.get("recipeId");
        String planDate = data.get("planDate");
        String mealType = data.get("mealType");

        if (mealPlanIdStr == null || recipeIdStr == null || planDate == null || mealType == null) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Missing required fields\"}");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int mealPlanId = Integer.parseInt(mealPlanIdStr);
            int recipeId = Integer.parseInt(recipeIdStr);
            
            conn = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO mealentry (mealplan_id, recipe_id, date, meal_type) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, mealPlanId);
            pstmt.setInt(2, recipeId);
            pstmt.setString(3, planDate);
            pstmt.setString(4, mealType);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int entryId = rs.getInt(1);
                    
                    pstmt.close();
                    pstmt = conn.prepareStatement("SELECT title FROM recipe WHERE recipe_id = ?");
                    pstmt.setInt(1, recipeId);
                    rs = pstmt.executeQuery();
                    String recipeTitle = "";
                    if (rs.next()) {
                        recipeTitle = rs.getString("title");
                    }
                    
                    String response = String.format(
                            "{\"id\":%d,\"mealPlanId\":%d,\"recipeId\":%d,\"planDate\":\"%s\",\"mealType\":\"%s\",\"recipeTitle\":\"%s\",\"message\":\"Meal entry added\"}",
                            entryId, mealPlanId, recipeId, planDate, WebServer.escapeJson(mealType), WebServer.escapeJson(recipeTitle));
                    WebServer.sendJsonResponse(exchange, 201, response);
                } else {
                    WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get entry ID\"}");
                }
            } else {
                WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to add meal entry\"}");
            }
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid ID format\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error: " + WebServer.escapeJson(e.getMessage()) + "\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        String entryIdStr = parts[parts.length - 1];

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            int entryId = Integer.parseInt(entryIdStr);
            conn = DatabaseConnection.getConnection();

            String deleteQuery = "DELETE FROM mealentry WHERE mealentry_id = ?";
            pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, entryId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                WebServer.sendJsonResponse(exchange, 200, "{\"message\":\"Meal entry deleted\"}");
            } else {
                WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"Meal entry not found\"}");
            }
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid entry ID\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }
}
