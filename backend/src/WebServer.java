import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebServer {

    private static final int PORT = 8080;

    private static final String CORS_ORIGIN = "http://localhost:3000"; // Change this to match your frontend port

    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", CORS_ORIGIN);
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // server.createContext("/api/users/change-password", new UserHandler());
        // server.createContext("/api/users/login", new UserHandler());
        // server.createContext("/api/users/signup", new UserHandler());
        // server.createContext("/api/users/", new UserDetailHandler());
        // server.createContext("/api/users", new UserHandler());
        // server.createContext("/api/recipes", new RecipesHandler());
        // server.createContext("/api/recipe/", new RecipeDetailHandler());
        // server.createContext("/api/search", new SearchHandler());
        // server.createContext("/api/mealplans", new MealPlansHandler());
        // server.createContext("/api/ingredients", new IngredientsHandler());
        // server.createContext("/api/stats", new StatsHandler());
        // server.createContext("/api/health-goals", new HealthGoalsHandler());
        // server.createContext("/", new StaticFileHandler());

        // USER AUTH
        server.createContext("/api/users/login", new UserHandler());
        server.createContext("/api/users/signup", new UserHandler());
        server.createContext("/api/users/change-password", new UserHandler());

        // USERS LIST + CREATE
        server.createContext("/api/users", new UserHandler());

        // USER DETAILS (like /api/users/1)
        server.createContext("/api/users/", new UserDetailHandler());

        // RECIPES
        server.createContext("/api/recipes", new RecipesHandler());
        server.createContext("/api/recipes/", new RecipeDetailHandler()); // <-- fixed

        // OTHER
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/mealplans", new MealPlansHandler());
        server.createContext("/api/ingredients", new IngredientsHandler());
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/health-goals", new HealthGoalsHandler());

        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("|| HEAL MEAL WEB SERVER STARTED ||");
        System.out.println("Server running at: http://localhost:" + PORT + "");
    }

    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File("web" + path);

            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(path);
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());

                OutputStream os = exchange.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = fs.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            } else {
                String response = "404 - File Not Found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private String getContentType(String path) {
            if (path.endsWith(".html"))
                return "text/html";
            if (path.endsWith(".css"))
                return "text/css";
            if (path.endsWith(".js"))
                return "application/javascript";
            if (path.endsWith(".json"))
                return "application/json";
            if (path.endsWith(".png"))
                return "image/png";
            if (path.endsWith(".jpg"))
                return "image/jpeg";
            return "text/plain";
        }
    }

    static class RecipesHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            if (!exchange.getRequestMethod().equals("GET")) {
                sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
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
                    if (!first)
                        json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",");
                    json.append("\"difficulty\":\"").append(escapeJson(rs.getString("difficulty"))).append("\",");
                    json.append("\"totalTime\":").append(rs.getInt("total_time")).append(",");
                    json.append("\"rating\":").append(Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0);
                    json.append("}");
                    first = false;
                }

                json.append("]");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✅ Served recipes list");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error: " + escapeJson(e.getMessage()) + "\"}");
            } finally {
                DatabaseConnection.closeResources(conn, stmt, rs);
            }
        }
    }

    static class RecipeDetailHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if (parts.length < 4) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Recipe ID required\"}");
                return;
            }

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            PreparedStatement ingStmt = null;
            ResultSet ingRs = null;
            PreparedStatement nutriStmt = null;
            ResultSet nutriRs = null;

            try {
                int recipeId = Integer.parseInt(parts[3]);
                conn = DatabaseConnection.getConnection();

                // Get recipe info
                String query = "SELECT * FROM recipe WHERE recipe_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, recipeId);
                rs = pstmt.executeQuery();

                if (!rs.next()) {
                    sendJsonResponse(exchange, 404, "{\"error\":\"Recipe not found\"}");
                    return;
                }

                StringBuilder json = new StringBuilder("{");
                json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                json.append("\"description\":\"").append(escapeJson(rs.getString("description"))).append("\",");
                json.append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",");
                json.append("\"difficulty\":\"").append(escapeJson(rs.getString("difficulty"))).append("\",");
                json.append("\"prepTime\":").append(rs.getInt("prep_time")).append(",");
                json.append("\"cookTime\":").append(rs.getInt("cook_time")).append(",");
                json.append("\"servings\":").append(rs.getInt("servings")).append(",");

                // Get ingredients
                String ingQuery = "SELECT i.name, ri.quantity, ri.unit " +
                        "FROM recipeingredient ri " +
                        "JOIN ingredient i ON ri.ingredient_id = i.ingredient_id " +
                        "WHERE ri.recipe_id = ?";
                ingStmt = conn.prepareStatement(ingQuery);
                ingStmt.setInt(1, recipeId);
                ingRs = ingStmt.executeQuery();

                json.append("\"ingredients\":[");
                boolean first = true;
                while (ingRs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{");
                    json.append("\"name\":\"").append(escapeJson(ingRs.getString("name"))).append("\",");
                    json.append("\"quantity\":").append(ingRs.getDouble("quantity")).append(",");
                    json.append("\"unit\":\"").append(escapeJson(ingRs.getString("unit"))).append("\"");
                    json.append("}");
                    first = false;
                }
                json.append("]");

                // Get nutrition
                String nutritionQuery = "SELECT " +
                        "SUM(i.calories_per_unit * ri.quantity) / r.servings as calories, " +
                        "SUM(i.protein * ri.quantity) / r.servings as protein, " +
                        "SUM(i.fat * ri.quantity) / r.servings as fat, " +
                        "SUM(i.carbs * ri.quantity) / r.servings as carbs " +
                        "FROM recipeingredient ri " +
                        "JOIN ingredient i ON ri.ingredient_id = i.ingredient_id " +
                        "JOIN recipe r ON ri.recipe_id = r.recipe_id " +
                        "WHERE ri.recipe_id = ?";
                nutriStmt = conn.prepareStatement(nutritionQuery);
                nutriStmt.setInt(1, recipeId);
                nutriRs = nutriStmt.executeQuery();

                if (nutriRs.next()) {
                    json.append(",\"nutrition\":{");
                    json.append("\"calories\":").append(Math.round(nutriRs.getDouble("calories"))).append(",");
                    json.append("\"protein\":").append(Math.round(nutriRs.getDouble("protein") * 10) / 10.0)
                            .append(",");
                    json.append("\"fat\":").append(Math.round(nutriRs.getDouble("fat") * 10) / 10.0).append(",");
                    json.append("\"carbs\":").append(Math.round(nutriRs.getDouble("carbs") * 10) / 10.0);
                    json.append("}");
                }

                json.append("}");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✓ Served recipe #" + recipeId + " with ingredients and nutrition");

            } catch (NumberFormatException e) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Invalid recipe ID\"}");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (pstmt != null)
                        pstmt.close();
                    if (ingRs != null)
                        ingRs.close();
                    if (ingStmt != null)
                        ingStmt.close();
                    if (nutriRs != null)
                        nutriRs.close();
                    if (nutriStmt != null)
                        nutriStmt.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class SearchHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("q=")) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Query parameter required\"}");
                return;
            }

            String searchTerm = URLDecoder.decode(query.substring(2), "UTF-8");

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.getConnection();
                String sql = "SELECT recipe_id, title, category, difficulty FROM recipe " +
                        "WHERE title LIKE ? OR description LIKE ? " +
                        "ORDER BY title LIMIT 20";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                rs = pstmt.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;

                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",");
                    json.append("\"difficulty\":\"").append(escapeJson(rs.getString("difficulty"))).append("\"");
                    json.append("}");
                    first = false;
                }

                json.append("]");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✓ Served search results for: " + searchTerm);

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
        }
    }

    static class MealPlansHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

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
                String query = "SELECT mealplan_id, user_id, title, start_date, end_date " +
                        "FROM mealplan ORDER BY start_date DESC LIMIT 10";

                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);

                StringBuilder json = new StringBuilder("[");
                boolean first = true;

                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("mealplan_id")).append(",");
                    json.append("\"userId\":").append(rs.getInt("user_id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"startDate\":\"").append(rs.getDate("start_date")).append("\",");
                    json.append("\"endDate\":\"").append(rs.getDate("end_date")).append("\"");
                    json.append("}");
                    first = false;
                }

                json.append("]");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✓ Served meal plans list");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
            } finally {
                DatabaseConnection.closeResources(conn, stmt, rs);
            }
        }
    }

    static class IngredientsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

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
                    if (!first)
                        json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("ingredient_id")).append(",");
                    json.append("\"name\":\"").append(escapeJson(rs.getString("name"))).append("\",");
                    json.append("\"unit\":\"").append(escapeJson(rs.getString("unit"))).append("\",");
                    json.append("\"calories\":").append(rs.getDouble("calories_per_unit")).append(",");
                    json.append("\"protein\":").append(rs.getDouble("protein")).append(",");
                    json.append("\"fat\":").append(rs.getDouble("fat")).append(",");
                    json.append("\"carbs\":").append(rs.getDouble("carbs"));
                    json.append("}");
                    first = false;
                }

                json.append("]");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✓ Served ingredients list");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
            } finally {
                DatabaseConnection.closeResources(conn, stmt, rs);
            }
        }
    }

    static class StatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

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

                // Count recipes
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM recipe");
                if (rs.next()) {
                    json.append("\"totalRecipes\":").append(rs.getInt("count")).append(",");
                }

                // Count ingredients
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM ingredient");
                if (rs.next()) {
                    json.append("\"totalIngredients\":").append(rs.getInt("count")).append(",");
                }

                // Count users
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM user");
                if (rs.next()) {
                    json.append("\"totalUsers\":").append(rs.getInt("count")).append(",");
                }

                // Count meal plans
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM mealplan");
                if (rs.next()) {
                    json.append("\"totalMealPlans\":").append(rs.getInt("count"));
                }

                json.append("}");

                sendJsonResponse(exchange, 200, json.toString());
                System.out.println("✓ Served database stats");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
            } finally {
                DatabaseConnection.closeResources(conn, stmt, rs);
            }
        }
    }

    static class UserHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("🔥🔥🔥 UserDetailHandler CALLED - Path: " + exchange.getRequestURI().getPath());
            System.out.println("🔥 Method: " + exchange.getRequestMethod());
            setCorsHeaders(exchange);

            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("🔥 UserHandler - Received " + requestMethod + " request to: " + path);

            if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            // POST requests (login, signup, change-password)
            if ("POST".equalsIgnoreCase(requestMethod)) {
                String requestBody;
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))) {
                    requestBody = reader.lines().collect(Collectors.joining("\n"));
                }

                System.out.println("📦 Request body: " + requestBody);

                // LOGIN
                if (path.contains("/login")) {
                    Map<String, String> data = parseJson(requestBody);
                    String email = data.get("email");
                    String password = data.get("password");

                    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                        sendJsonResponse(exchange, 400, "{\"error\":\"Missing email or password\"}");
                        return;
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;

                    try {
                        conn = DatabaseConnection.getConnection();
                        String query = "SELECT user_id, name, email, diet_type FROM user WHERE email = ? AND password = ?";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, email);
                        pstmt.setString(2, password);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            int userId = rs.getInt("user_id");
                            String userName = rs.getString("name");
                            String userEmail = rs.getString("email");
                            String dietType = rs.getString("diet_type");

                            System.out.println("✅ User logged in: " + email + " (ID: " + userId + ")");

                            // IMPORTANT: Include ALL user data in the response
                            String response = String.format(
                                    "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"message\":\"Login successful\"}",
                                    userId,
                                    escapeJson(userName),
                                    escapeJson(userEmail),
                                    escapeJson(dietType != null ? dietType : "none"));
                            sendJsonResponse(exchange, 200, response);
                        } else {
                            System.out.println("❌ Login failed for: " + email);
                            sendJsonResponse(exchange, 401, "{\"error\":\"Invalid email or password\"}");
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Login database error: " + e.getMessage());
                        e.printStackTrace();
                        sendJsonResponse(exchange, 500, "{\"error\":\"Database error during login\"}");
                    } finally {
                        DatabaseConnection.closeResources(conn, pstmt, rs);
                    }
                    return;
                }

                // SIGNUP
                else if (path.contains("/signup")) {
                    Map<String, String> data = parseJson(requestBody);
                    String name = data.get("name");
                    String email = data.get("email");
                    String password = data.get("password");
                    String dietType = data.get("diet_type");

                    if (name == null || email == null || password == null || dietType == null ||
                            name.isEmpty() || email.isEmpty() || password.isEmpty() || dietType.isEmpty()) {
                        sendJsonResponse(exchange, 400, "{\"error\":\"All fields are required\"}");
                        return;
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;

                    try {
                        conn = DatabaseConnection.getConnection();

                        String checkQuery = "SELECT user_id FROM user WHERE email = ?";
                        pstmt = conn.prepareStatement(checkQuery);
                        pstmt.setString(1, email);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            sendJsonResponse(exchange, 409, "{\"error\":\"Email already registered\"}");
                            return;
                        }
                        pstmt.close();

                        String insertQuery = "INSERT INTO user (name, email, password, diet_type, created_at) VALUES (?, ?, ?, ?, NOW())";
                        pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                        pstmt.setString(1, name);
                        pstmt.setString(2, email);
                        pstmt.setString(3, password);
                        pstmt.setString(4, dietType);

                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            rs = pstmt.getGeneratedKeys();
                            if (rs.next()) {
                                int userId = rs.getInt(1);
                                System.out.println("✅ User registered: " + email + " (ID: " + userId + ")");

                                String response = String.format(
                                        "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"message\":\"Registration successful\"}",
                                        userId,
                                        escapeJson(name),
                                        escapeJson(email),
                                        escapeJson(dietType));
                                sendJsonResponse(exchange, 201, response);
                            } else {
                                sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get user ID\"}");
                            }
                        } else {
                            sendJsonResponse(exchange, 500, "{\"error\":\"Registration failed\"}");
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Signup database error: " + e.getMessage());
                        e.printStackTrace();

                        if (e.getMessage().contains("Duplicate entry")) {
                            sendJsonResponse(exchange, 409, "{\"error\":\"Email already registered\"}");
                        } else {
                            sendJsonResponse(exchange, 500, "{\"error\":\"Database error during registration\"}");
                        }
                    } finally {
                        DatabaseConnection.closeResources(conn, pstmt, rs);
                    }
                    return;
                }

                // CHANGE PASSWORD
                else if (path.contains("/change-password")) {
                    Map<String, String> data = parseJson(requestBody);
                    String userId = data.get("userId");
                    String currentPassword = data.get("currentPassword");
                    String newPassword = data.get("newPassword");

                    if (userId == null || currentPassword == null || newPassword == null ||
                            userId.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
                        sendJsonResponse(exchange, 400, "{\"error\":\"All fields are required\"}");
                        return;
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;

                    try {
                        conn = DatabaseConnection.getConnection();

                        String checkQuery = "SELECT user_id FROM user WHERE user_id = ? AND password = ?";
                        pstmt = conn.prepareStatement(checkQuery);
                        pstmt.setInt(1, Integer.parseInt(userId));
                        pstmt.setString(2, currentPassword);
                        rs = pstmt.executeQuery();

                        if (!rs.next()) {
                            sendJsonResponse(exchange, 401, "{\"error\":\"Current password is incorrect\"}");
                            return;
                        }
                        pstmt.close();

                        String updateQuery = "UPDATE user SET password = ? WHERE user_id = ?";
                        pstmt = conn.prepareStatement(updateQuery);
                        pstmt.setString(1, newPassword);
                        pstmt.setInt(2, Integer.parseInt(userId));

                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("✅ Password changed for user ID: " + userId);
                            sendJsonResponse(exchange, 200, "{\"message\":\"Password changed successfully\"}");
                        } else {
                            sendJsonResponse(exchange, 500, "{\"error\":\"Failed to change password\"}");
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Password change error: " + e.getMessage());
                        e.printStackTrace();
                        sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
                    } finally {
                        DatabaseConnection.closeResources(conn, pstmt, rs);
                    }
                    return;
                }

                else {
                    sendJsonResponse(exchange, 404, "{\"error\":\"Endpoint not found\"}");
                    return;
                }
            }

            // GET /api/users - List all users
            else if ("GET".equalsIgnoreCase(requestMethod)) {
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseConnection.getConnection();
                    String query = "SELECT user_id, name, email, diet_type, created_at FROM user ORDER BY user_id ASC LIMIT 50";

                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(query);

                    StringBuilder json = new StringBuilder("[");
                    boolean first = true;

                    while (rs.next()) {
                        if (!first)
                            json.append(",");
                        json.append("{");
                        json.append("\"id\":").append(rs.getInt("user_id")).append(",");
                        json.append("\"name\":\"").append(escapeJson(rs.getString("name"))).append("\",");
                        json.append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",");
                        json.append("\"dietType\":\"").append(escapeJson(rs.getString("diet_type"))).append("\"");
                        json.append("}");
                        first = false;
                    }
                    json.append("]");

                    sendJsonResponse(exchange, 200, json.toString());
                    System.out.println("✅ Served users list");
                } catch (SQLException e) {
                    System.err.println("Database error: " + e.getMessage());
                    e.printStackTrace();
                    sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
                } finally {
                    DatabaseConnection.closeResources(conn, stmt, rs);
                }
                return;
            }

            else {
                sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    static class UserDetailHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("🔥🔥🔥 UserDetailHandler CALLED - Path: " + exchange.getRequestURI().getPath());
            setCorsHeaders(exchange);

            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("🔥 UserDetailHandler - Received " + requestMethod + " request to: " + path);

            if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            // Extract user ID from path
            String[] parts = path.split("/");
            if (parts.length < 4) {
                sendJsonResponse(exchange, 400, "{\"error\":\"User ID required\"}");
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(parts[3]);
                System.out.println("👤 Extracted user ID: " + userId);
            } catch (NumberFormatException e) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Invalid user ID\"}");
                return;
            }

            if ("GET".equalsIgnoreCase(requestMethod)) {
                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseConnection.getConnection();
                    String query = "SELECT user_id, name, email, diet_type, created_at FROM user WHERE user_id = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, userId);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String response = String.format(
                                "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"created_at\":\"%s\"}",
                                rs.getInt("user_id"),
                                escapeJson(rs.getString("name")),
                                escapeJson(rs.getString("email")),
                                escapeJson(rs.getString("diet_type") != null ? rs.getString("diet_type") : "none"),
                                rs.getTimestamp("created_at").toString());
                        sendJsonResponse(exchange, 200, response);
                        System.out.println("✅ Served user profile for ID: " + userId);
                    } else {
                        sendJsonResponse(exchange, 404, "{\"error\":\"User not found\"}");
                    }
                } catch (SQLException e) {
                    System.err.println("❌ Database error: " + e.getMessage());
                    e.printStackTrace();
                    sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
                } finally {
                    DatabaseConnection.closeResources(conn, pstmt, rs);
                }
                return;
            }

            else if ("PUT".equalsIgnoreCase(requestMethod)) {
                String requestBody;
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))) {
                    requestBody = reader.lines().collect(Collectors.joining("\n"));
                }

                Map<String, String> data = parseJson(requestBody);
                String name = data.get("name");
                String email = data.get("email");
                String dietType = data.get("diet_type");

                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                    conn = DatabaseConnection.getConnection();
                    String query = "UPDATE user SET name = ?, email = ?, diet_type = ? WHERE user_id = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, dietType);
                    pstmt.setInt(4, userId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        String response = String.format(
                                "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"message\":\"Profile updated successfully\"}",
                                userId,
                                escapeJson(name),
                                escapeJson(email),
                                escapeJson(dietType));
                        sendJsonResponse(exchange, 200, response);
                        System.out.println("✅ Updated profile for user ID: " + userId);
                    } else {
                        sendJsonResponse(exchange, 404, "{\"error\":\"User not found\"}");
                    }
                } catch (SQLException e) {
                    System.err.println("❌ Update error: " + e.getMessage());
                    e.printStackTrace();
                    sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
                } finally {
                    DatabaseConnection.closeResources(conn, pstmt, null);
                }
                return;
            }

            else {
                sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    static class HealthGoalsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("🎯🎯🎯 HealthGoalsHandler CALLED - Path: " + exchange.getRequestURI().getPath());

            setCorsHeaders(exchange);

            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            System.out.println("🎯 HealthGoalsHandler - " + requestMethod + " to: " + path);

            if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            // GET /api/health-goals?userId={id} - Get user's health goals
            if ("GET".equalsIgnoreCase(requestMethod)) {
                if (query == null || !query.startsWith("userId=")) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"userId parameter required\"}");
                    return;
                }

                String userIdStr = query.substring(7); // Remove "userId="
                int userId;
                try {
                    userId = Integer.parseInt(userIdStr);
                } catch (NumberFormatException e) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Invalid userId\"}");
                    return;
                }

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseConnection.getConnection();

                    // Get the most recent health goal for this user
                    String query1 = "SELECT goal_id, user_id, daily_calorie_target, protein_target, " +
                            "carb_target, fat_target, goal_type, start_date " +
                            "FROM user_health_goal " +
                            "WHERE user_id = ? " +
                            "ORDER BY start_date DESC, goal_id DESC " +
                            "LIMIT 1";

                    pstmt = conn.prepareStatement(query1);
                    pstmt.setInt(1, userId);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String response = String.format(
                                "{\"goalId\":%d,\"userId\":%d,\"calorieGoal\":%d,\"proteinGoal\":%d,\"carbsGoal\":%d,\"fatsGoal\":%d,\"goalType\":\"%s\",\"startDate\":\"%s\"}",
                                rs.getInt("goal_id"),
                                rs.getInt("user_id"),
                                rs.getInt("daily_calorie_target"),
                                Math.round(rs.getFloat("protein_target")), 
                                Math.round(rs.getFloat("carb_target")), 
                                Math.round(rs.getFloat("fat_target")), 
                                escapeJson(rs.getString("goal_type") != null ? rs.getString("goal_type") : ""),
                                rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "");
                        System.out.println("🔍 DEBUG - Health Goals JSON: " + response); 
                        sendJsonResponse(exchange, 200, response);
                        System.out.println("✅ Served health goals for user: " + userId);
                    } else {
                        String response = String.format(
                                "{\"userId\":%d,\"calorieGoal\":2000,\"proteinGoal\":150.0,\"carbsGoal\":200.0,\"fatsGoal\":65.0,\"goalType\":\"\",\"message\":\"No goals set\"}",
                                userId);
                        sendJsonResponse(exchange, 200, response);
                        System.out.println("⚠️ No health goals found for user: " + userId);
                    }
                } catch (SQLException e) {
                    System.err.println("❌ Database error: " + e.getMessage());
                    e.printStackTrace();
                    sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
                } finally {
                    DatabaseConnection.closeResources(conn, pstmt, rs);
                }
                return;
            }

            // POST /api/health-goals - Create or update health goals
            else if ("POST".equalsIgnoreCase(requestMethod)) {
                String requestBody;
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))) {
                    requestBody = reader.lines().collect(Collectors.joining("\n"));
                }

                System.out.println("📦 Request body: " + requestBody);

                Map<String, String> data = parseJson(requestBody);
                System.out.println("🔍 Parsed data: " + data); 

                String userIdStr = data.get("userId");
                String calorieGoalStr = data.get("calorieGoal");
                String proteinGoalStr = data.get("proteinGoal");
                String carbsGoalStr = data.get("carbsGoal");
                String fatsGoalStr = data.get("fatsGoal");
                String goalType = data.get("goalType");

                System.out.println("🔍 userId: " + userIdStr);
                System.out.println("🔍 calorieGoal: " + calorieGoalStr); 

                if (userIdStr == null || calorieGoalStr == null || proteinGoalStr == null ||
                        carbsGoalStr == null || fatsGoalStr == null) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Missing required fields\"}");
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
                    sendJsonResponse(exchange, 400, "{\"error\":\"Invalid number format\"}");
                    return;
                }

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseConnection.getConnection();

                    // Check if user already has goals
                    String checkQuery = "SELECT goal_id FROM user_health_goal WHERE user_id = ? ORDER BY start_date DESC LIMIT 1";
                    pstmt = conn.prepareStatement(checkQuery);
                    pstmt.setInt(1, userId);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        // Update existing goal
                        int goalId = rs.getInt("goal_id");
                        pstmt.close();

                        String updateQuery = "UPDATE user_health_goal SET " +
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
                            sendJsonResponse(exchange, 200, response);
                            System.out.println("✅ Updated health goals for user: " + userId);
                        } else {
                            sendJsonResponse(exchange, 500, "{\"error\":\"Failed to update goals\"}");
                        }
                    } else {
                        // Insert new goal
                        pstmt.close();

                        String insertQuery = "INSERT INTO user_health_goal " +
                                "(user_id, daily_calorie_target, protein_target, carb_target, fat_target, goal_type, start_date) "
                                +
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
                                        "{\"goalId\":%d,\"userId\":%d,\"calorieGoal\":%d,\"proteinGoal\":%d,\"carbsGoal\":%d,\"fatsGoal\":%d,\"message\":\"Goals updated successfully\"}",
                                        goalId, userId, calorieGoal,
                                        Math.round(proteinGoal),
                                        Math.round(carbsGoal),
                                        Math.round(fatsGoal));
                                sendJsonResponse(exchange, 200, response);
                                System.out.println("✅ Created health goals for user: " + userId);
                            } else {
                                sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get goal ID\"}");
                            }
                        } else {
                            sendJsonResponse(exchange, 500, "{\"error\":\"Failed to create goals\"}");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("❌ Database error: " + e.getMessage());
                    e.printStackTrace();
                    sendJsonResponse(exchange, 500,
                            "{\"error\":\"Database error: " + escapeJson(e.getMessage()) + "\"}");
                } finally {
                    DatabaseConnection.closeResources(conn, pstmt, rs);
                }
                return;
            }

            else {
                sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        try {
            json = json.trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1);

                boolean inQuotes = false;
                StringBuilder currentPair = new StringBuilder();

                for (int i = 0; i < json.length(); i++) {
                    char c = json.charAt(i);

                    if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                        inQuotes = !inQuotes;
                    }

                    if (c == ',' && !inQuotes) {
                        processPair(currentPair.toString(), map);
                        currentPair = new StringBuilder();
                    } else {
                        currentPair.append(c);
                    }
                }

                if (currentPair.length() > 0) {
                    processPair(currentPair.toString(), map);
                }
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    private static void processPair(String pair, Map<String, String> map) {
        String[] keyValue = pair.trim().split(":", 2);
        if (keyValue.length == 2) {
            String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
            String value = keyValue[1].trim().replaceAll("^\"|\"$", "");
            map.put(key, value);
        }
    }

    private static String escapeJson(String str) {
        if (str == null)
            return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
