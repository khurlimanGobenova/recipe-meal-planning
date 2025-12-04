import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class NutritionProgressHandler implements HttpHandler {
    
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

            // Get user's health goals
            String goalsQuery = "SELECT daily_calorie_target, protein_target, carb_target, fat_target " +
                    "FROM userhealthgoal WHERE user_id = ? ORDER BY start_date DESC LIMIT 1";
            pstmt = conn.prepareStatement(goalsQuery);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            int calorieGoal = 2000;
            double proteinGoal = 150;
            double carbGoal = 200;
            double fatGoal = 65;

            if (rs.next()) {
                calorieGoal = rs.getInt("daily_calorie_target");
                proteinGoal = rs.getDouble("protein_target");
                carbGoal = rs.getDouble("carb_target");
                fatGoal = rs.getDouble("fat_target");
            }

            // Calculate current nutrition from today's meal entries
            pstmt.close();
            String nutritionQuery = "SELECT " +
                    "SUM(i.calories_per_unit * ri.quantity) as total_calories, " +
                    "SUM(i.protein * ri.quantity) as total_protein, " +
                    "SUM(i.carbs * ri.quantity) as total_carbs, " +
                    "SUM(i.fat * ri.quantity) as total_fat " +
                    "FROM mealentry me " +
                    "JOIN mealplan mp ON me.mealplan_id = mp.mealplan_id " +
                    "JOIN recipe r ON me.recipe_id = r.recipe_id " +
                    "JOIN recipeingredient ri ON r.recipe_id = ri.recipe_id " +
                    "JOIN ingredient i ON ri.ingredient_id = i.ingredient_id " +
                    "WHERE mp.user_id = ? AND me.date = CURDATE()";

            pstmt = conn.prepareStatement(nutritionQuery);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            double totalCalories = 0;
            double totalProtein = 0;
            double totalCarbs = 0;
            double totalFat = 0;

            if (rs.next()) {
                totalCalories = rs.getDouble("total_calories");
                totalProtein = rs.getDouble("total_protein");
                totalCarbs = rs.getDouble("total_carbs");
                totalFat = rs.getDouble("total_fat");
            }

            // Calculate percentages
            int caloriePercent = calorieGoal > 0 ? (int)((totalCalories / calorieGoal) * 100) : 0;
            int proteinPercent = proteinGoal > 0 ? (int)((totalProtein / proteinGoal) * 100) : 0;
            int carbPercent = carbGoal > 0 ? (int)((totalCarbs / carbGoal) * 100) : 0;
            int fatPercent = fatGoal > 0 ? (int)((totalFat / fatGoal) * 100) : 0;

            String response = String.format(
                    "{\"calories\":{\"current\":%d,\"goal\":%d,\"percent\":%d}," +
                    "\"protein\":{\"current\":%d,\"goal\":%d,\"percent\":%d}," +
                    "\"carbs\":{\"current\":%d,\"goal\":%d,\"percent\":%d}," +
                    "\"fat\":{\"current\":%d,\"goal\":%d,\"percent\":%d}}",
                    (int)totalCalories, calorieGoal, caloriePercent,
                    (int)totalProtein, (int)proteinGoal, proteinPercent,
                    (int)totalCarbs, (int)carbGoal, carbPercent,
                    (int)totalFat, (int)fatGoal, fatPercent);

            WebServer.sendJsonResponse(exchange, 200, response);

        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid user ID\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
