import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class MealPlanHandler implements HttpHandler {
    
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
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            handlePost(exchange);
        } else {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sqlQuery;
            
            if (query != null && query.startsWith("userId=")) {
                String userIdStr = query.substring(7);
                int userId = Integer.parseInt(userIdStr);
                sqlQuery = "SELECT mealplan_id, user_id, title, start_date, end_date " +
                        "FROM mealplan WHERE user_id = ? ORDER BY start_date DESC";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setInt(1, userId);
            } else {
                sqlQuery = "SELECT mealplan_id, user_id, title, start_date, end_date " +
                        "FROM mealplan ORDER BY start_date DESC LIMIT 10";
                pstmt = conn.prepareStatement(sqlQuery);
            }

            rs = pstmt.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{");
                json.append("\"id\":").append(rs.getInt("mealplan_id")).append(",");
                json.append("\"userId\":").append(rs.getInt("user_id")).append(",");
                json.append("\"title\":\"").append(WebServer.escapeJson(rs.getString("title"))).append("\",");
                json.append("\"startDate\":\"").append(rs.getDate("start_date")).append("\",");
                json.append("\"endDate\":\"").append(rs.getDate("end_date")).append("\"");
                json.append("}");
                first = false;
            }

            json.append("]");
            WebServer.sendJsonResponse(exchange, 200, json.toString());

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
        String userIdStr = data.get("userId");
        String title = data.get("title");
        String startDate = data.get("startDate");
        String endDate = data.get("endDate");

        if (userIdStr == null || title == null || startDate == null || endDate == null) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Missing required fields\"}");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int userId = Integer.parseInt(userIdStr);
            conn = DatabaseConnection.getConnection();

            String insertQuery = "INSERT INTO mealplan (user_id, title, start_date, end_date) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int mealPlanId = rs.getInt(1);
                    String response = String.format(
                            "{\"id\":%d,\"userId\":%d,\"title\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\",\"message\":\"Meal plan created successfully\"}",
                            mealPlanId, userId, WebServer.escapeJson(title), startDate, endDate);
                    WebServer.sendJsonResponse(exchange, 201, response);
                } else {
                    WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get meal plan ID\"}");
                }
            } else {
                WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to create meal plan\"}");
            }
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid user ID\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error: " + WebServer.escapeJson(e.getMessage()) + "\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
