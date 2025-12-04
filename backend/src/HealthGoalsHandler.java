import java.io.*;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class HealthGoalsHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if ("GET".equalsIgnoreCase(requestMethod)) {
            handleGet(exchange, query);
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            handlePost(exchange);
        } else {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query == null || !query.startsWith("userId=")) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"userId parameter required\"}");
            return;
        }

        String userIdStr = query.substring(7);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int userId = Integer.parseInt(userIdStr);
            conn = DatabaseConnection.getConnection();

            String sqlQuery = "SELECT goal_id, daily_calorie_target, protein_target, carb_target, fat_target, goal_type " +
                    "FROM userhealthgoal WHERE user_id = ? ORDER BY start_date DESC LIMIT 1";

            pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String response = String.format(
                        "{\"goalId\":%d,\"calorieGoal\":%d,\"proteinGoal\":%d,\"carbsGoal\":%d,\"fatsGoal\":%d,\"goalType\":\"%s\"}",
                        rs.getInt("goal_id"),
                        rs.getInt("daily_calorie_target"),
                        Math.round(rs.getFloat("protein_target")),
                        Math.round(rs.getFloat("carb_target")),
                        Math.round(rs.getFloat("fat_target")),
                        WebServer.escapeJson(rs.getString("goal_type") != null ? rs.getString("goal_type") : ""));
                WebServer.sendJsonResponse(exchange, 200, response);
            } else {
                WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"No health goals found for user\"}");
            }
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid user ID\"}");
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
        String calorieGoalStr = data.get("calorieGoal");
        String proteinGoalStr = data.get("proteinGoal");
        String carbsGoalStr = data.get("carbsGoal");
        String fatsGoalStr = data.get("fatsGoal");
        String goalType = data.get("goalType");

        if (userIdStr == null || calorieGoalStr == null || proteinGoalStr == null || 
            carbsGoalStr == null || fatsGoalStr == null) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Missing required fields\"}");
            return;
        }

        int userId;
        int calorieGoal;
        float proteinGoal;
        float carbsGoal;
        float fatsGoal;

        try {
            userId = Integer.parseInt(userIdStr);
            calorieGoal = Integer.parseInt(calorieGoalStr);
            proteinGoal = Float.parseFloat(proteinGoalStr);
            carbsGoal = Float.parseFloat(carbsGoalStr);
            fatsGoal = Float.parseFloat(fatsGoalStr);
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid number format\"}");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            String checkQuery = "SELECT goal_id FROM userhealthgoal WHERE user_id = ? ORDER BY start_date DESC LIMIT 1";
            pstmt = conn.prepareStatement(checkQuery);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int goalId = rs.getInt("goal_id");
                pstmt.close();

                String updateQuery = "UPDATE userhealthgoal SET " +
                        "daily_calorie_target = ?, " +
                        "protein_target = ?, " +
                        "carb_target = ?, " +
                        "fat_target = ?, " +
                        "goal_type = ? " +
                        "WHERE goal_id = ?";

                pstmt = conn.prepareStatement(updateQuery);
                pstmt.setInt(1, calorieGoal);
                pstmt.setFloat(2, proteinGoal);
                pstmt.setFloat(3, carbsGoal);
                pstmt.setFloat(4, fatsGoal);
                pstmt.setString(5, goalType != null ? goalType : "");
                pstmt.setInt(6, goalId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    String response = String.format(
                            "{\"goalId\":%d,\"userId\":%d,\"calorieGoal\":%d,\"proteinGoal\":%d,\"carbsGoal\":%d,\"fatsGoal\":%d,\"message\":\"Goals updated successfully\"}",
                            goalId, userId, calorieGoal,
                            Math.round(proteinGoal),
                            Math.round(carbsGoal),
                            Math.round(fatsGoal));
                    WebServer.sendJsonResponse(exchange, 200, response);
                } else {
                    WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to update goals\"}");
                }
            } else {
                pstmt.close();

                String insertQuery = "INSERT INTO userhealthgoal " +
                        "(user_id, daily_calorie_target, protein_target, carb_target, fat_target, goal_type, start_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, CURDATE())";

                pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, calorieGoal);
                pstmt.setFloat(3, proteinGoal);
                pstmt.setFloat(4, carbsGoal);
                pstmt.setFloat(5, fatsGoal);
                pstmt.setString(6, goalType != null ? goalType : "");

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        int goalId = rs.getInt(1);
                        String response = String.format(
                                "{\"goalId\":%d,\"userId\":%d,\"calorieGoal\":%d,\"proteinGoal\":%d,\"carbsGoal\":%d,\"fatsGoal\":%d,\"message\":\"Goals created successfully\"}",
                                goalId, userId, calorieGoal,
                                Math.round(proteinGoal),
                                Math.round(carbsGoal),
                                Math.round(fatsGoal));
                        WebServer.sendJsonResponse(exchange, 200, response);
                    } else {
                        WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get goal ID\"}");
                    }
                } else {
                    WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to create goals\"}");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500,
                    "{\"error\":\"Database error: " + WebServer.escapeJson(e.getMessage()) + "\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
