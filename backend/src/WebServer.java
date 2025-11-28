import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement; // Needed for Sign Up logic
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class WebServer {

    private static final int PORT = 8080;

    private static final String CORS_ORIGIN = "http://localhost:5173"; // Change this to match your frontend port

    // THEN CREATE THIS HELPER METHOD to set CORS consistently everywhere
    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", CORS_ORIGIN);
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new StaticFileHandler());

        // api endpoints
        server.createContext("/api/recipes", new RecipesHandler());
        server.createContext("/api/recipe/", new RecipeDetailHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/mealplans", new MealPlansHandler());
        server.createContext("/api/ingredients", new IngredientsHandler());
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/users", new UserHandler()); // ADDED THIS LINE

        server.setExecutor(null);
        server.start();

        System.out.println("|| HEAL MEAL WEB SERVER STARTED ||");
        System.out.println("Server running at: http://localhost:" + PORT + "");

    }

    // Handler for static files (HTML, CSS, JS)
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

    // api: get all recipes - FIXED
    // Replace your RecipesHandler with this:

    static class RecipesHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // USE THE HELPER METHOD for consistent CORS
            setCorsHeaders(exchange);

            // Handle OPTIONS
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

    // api: get recipe details
    static class RecipeDetailHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Add this for POST/PUT/DELETE handlers:
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

    // api: search recipes
    static class SearchHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Add this for POST/PUT/DELETE handlers:
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

    // api: get meal plans
    static class MealPlansHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Add this for POST/PUT/DELETE handlers:
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

    // api: get ingredients
    static class IngredientsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Add this for POST/PUT/DELETE handlers:
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

    // api: get database stats
    static class StatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Add this for POST/PUT/DELETE handlers:
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

    // api: get users
    // api: get users
    // Replace your UserHandler class with this fixed version

    static class UserHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            // USE THE HELPER METHOD
            setCorsHeaders(exchange);

            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("📥 Received " + requestMethod + " request to: " + path);

            // Handle OPTIONS (Pre-flight)
            if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            // HANDLE POST REQUESTS (LOGIN / SIGNUP)
            if ("POST".equalsIgnoreCase(requestMethod)) {
                String requestBody;
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))) {
                    requestBody = reader.lines().collect(Collectors.joining("\n"));
                }

                System.out.println("📦 Request body: " + requestBody);

                // LOGIN ENDPOINT
                if (path.contains("/login")) {
                    Map<String, String> data = parseJson(requestBody);
                    String email = data.get("email");
                    String password = data.get("password");

                    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                        sendJsonResponse(exchange, 400,
                                "{\"error\":\"Missing email or password\"}");
                        return;
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;

                    try {
                        conn = DatabaseConnection.getConnection();
                        String query = "SELECT user_id, name FROM user WHERE email = ? AND password = ?";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, email);
                        pstmt.setString(2, password);

                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            int userId = rs.getInt("user_id");
                            String userName = rs.getString("name");

                            System.out.println("✅ User logged in: " + email + " (ID: " + userId + ")");

                            String response = String.format(
                                    "{\"userId\":%d,\"name\":\"%s\",\"message\":\"Login successful\"}",
                                    userId, escapeJson(userName));
                            sendJsonResponse(exchange, 200, response);
                        } else {
                            System.out.println("❌ Login failed for: " + email);
                            sendJsonResponse(exchange, 401,
                                    "{\"error\":\"Invalid email or password\"}");
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Login database error: " + e.getMessage());
                        e.printStackTrace();
                        sendJsonResponse(exchange, 500,
                                "{\"error\":\"Database error during login\"}");
                    } finally {
                        DatabaseConnection.closeResources(conn, pstmt, rs);
                    }
                    return;
                }

                // SIGNUP ENDPOINT
                else if (path.contains("/signup")) {
                    Map<String, String> data = parseJson(requestBody);
                    String name = data.get("name");
                    String email = data.get("email");
                    String password = data.get("password");
                    String dietType = data.get("diet_type");

                    if (name == null || email == null || password == null || dietType == null ||
                            name.isEmpty() || email.isEmpty() || password.isEmpty() || dietType.isEmpty()) {
                        sendJsonResponse(exchange, 400,
                                "{\"error\":\"All fields are required\"}");
                        return;
                    }

                    Connection conn = null;
                    PreparedStatement pstmt = null;
                    ResultSet rs = null;

                    try {
                        conn = DatabaseConnection.getConnection();

                        // Check if email already exists
                        String checkQuery = "SELECT user_id FROM user WHERE email = ?";
                        pstmt = conn.prepareStatement(checkQuery);
                        pstmt.setString(1, email);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            sendJsonResponse(exchange, 409,
                                    "{\"error\":\"Email already registered\"}");
                            return;
                        }

                        // Close previous statement
                        pstmt.close();

                        // Insert new user
                        String insertQuery = "INSERT INTO user (name, email, password, diet_type, created_at) " +
                                "VALUES (?, ?, ?, ?, NOW())";
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
                                        "{\"userId\":%d,\"message\":\"Registration successful\"}",
                                        userId);
                                sendJsonResponse(exchange, 201, response);
                            } else {
                                sendJsonResponse(exchange, 500,
                                        "{\"error\":\"Failed to get user ID\"}");
                            }
                        } else {
                            sendJsonResponse(exchange, 500,
                                    "{\"error\":\"Registration failed\"}");
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Signup database error: " + e.getMessage());
                        e.printStackTrace();

                        if (e.getMessage().contains("Duplicate entry")) {
                            sendJsonResponse(exchange, 409,
                                    "{\"error\":\"Email already registered\"}");
                        } else {
                            sendJsonResponse(exchange, 500,
                                    "{\"error\":\"Database error during registration\"}");
                        }
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

            // HANDLE GET REQUESTS
            else if ("GET".equalsIgnoreCase(requestMethod)) {
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                try {
                    conn = DatabaseConnection.getConnection();
                    String query = "SELECT user_id, name, email, diet_type, created_at " +
                            "FROM user ORDER BY user_id ASC LIMIT 50";

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

    // Replace the sendJsonResponse and parseJson methods at the bottom of
    // WebServer.java

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

                // Better parsing that handles quoted values with special characters
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

                // Process last pair
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
