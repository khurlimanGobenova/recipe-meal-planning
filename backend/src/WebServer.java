import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class WebServer {
    
    private static final int PORT = 8080;
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Serve static files (HTML, CSS, JS)
        server.createContext("/", new StaticFileHandler());
        
        // API endpoints
        server.createContext("/api/recipes", new RecipesHandler());
        server.createContext("/api/recipe/", new RecipeDetailHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/mealplans", new MealPlansHandler());
        server.createContext("/api/ingredients", new IngredientsHandler());
        server.createContext("/api/stats", new StatsHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   HEAL MEAL WEB SERVER STARTED                 ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ Server running at: http://localhost:" + PORT + "       ║");
        System.out.println("║ Press Ctrl+C to stop the server                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
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
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg")) return "image/jpeg";
            return "text/plain";
        }
    }
    
    // API: Get all recipes
    static class RecipesHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT r.recipe_id, r.title, r.category, r.difficulty, " +
                              "(r.prep_time + r.cook_time) as total_time, " +
                              "AVG(ra.rating) as avg_rating " +
                              "FROM recipe r " +
                              "LEFT JOIN rating ra ON r.recipe_id = ra.recipe_id " +
                              "GROUP BY r.recipe_id " +
                              "ORDER BY r.title " +
                              "LIMIT 50";
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                
                while (rs.next()) {
                    if (!first) json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",");
                    json.append("\"difficulty\":\"").append(escapeJson(rs.getString("difficulty"))).append("\",");
                    json.append("\"totalTime\":").append(rs.getInt("total_time")).append(",");
                    json.append("\"rating\":").append(rs.getDouble("avg_rating"));
                    json.append("}");
                    first = false;
                }
                
                json.append("]");
                
                rs.close();
                stmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error: " + e.getMessage() + "\"}");
            }
        }
    }
    
    // API: Get recipe details
    static class RecipeDetailHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            
            if (parts.length < 4) {
                sendResponse(exchange, 400, "{\"error\":\"Recipe ID required\"}");
                return;
            }
            
            try {
                int recipeId = Integer.parseInt(parts[3]);
                Connection conn = DatabaseConnection.getConnection();
                
                // Get recipe info
                String query = "SELECT * FROM recipe WHERE recipe_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, recipeId);
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    sendResponse(exchange, 404, "{\"error\":\"Recipe not found\"}");
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
                PreparedStatement ingStmt = conn.prepareStatement(ingQuery);
                ingStmt.setInt(1, recipeId);
                ResultSet ingRs = ingStmt.executeQuery();
                
                json.append("\"ingredients\":[");
                boolean first = true;
                while (ingRs.next()) {
                    if (!first) json.append(",");
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
                PreparedStatement nutriStmt = conn.prepareStatement(nutritionQuery);
                nutriStmt.setInt(1, recipeId);
                ResultSet nutriRs = nutriStmt.executeQuery();
                
                if (nutriRs.next()) {
                    json.append(",\"nutrition\":{");
                    json.append("\"calories\":").append(Math.round(nutriRs.getDouble("calories"))).append(",");
                    json.append("\"protein\":").append(Math.round(nutriRs.getDouble("protein") * 10) / 10.0).append(",");
                    json.append("\"fat\":").append(Math.round(nutriRs.getDouble("fat") * 10) / 10.0).append(",");
                    json.append("\"carbs\":").append(Math.round(nutriRs.getDouble("carbs") * 10) / 10.0);
                    json.append("}");
                }
                
                json.append("}");
                
                ingRs.close();
                ingStmt.close();
                nutriRs.close();
                nutriStmt.close();
                rs.close();
                pstmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"error\":\"Invalid recipe ID\"}");
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error\"}");
            }
        }
    }
    
    // API: Search recipes
    static class SearchHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("q=")) {
                sendResponse(exchange, 400, "{\"error\":\"Query parameter required\"}");
                return;
            }
            
            String searchTerm = URLDecoder.decode(query.substring(2), "UTF-8");
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                String sql = "SELECT recipe_id, title, category, difficulty FROM recipe " +
                            "WHERE title LIKE ? OR description LIKE ? " +
                            "ORDER BY title LIMIT 20";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                ResultSet rs = pstmt.executeQuery();
                
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                
                while (rs.next()) {
                    if (!first) json.append(",");
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("recipe_id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",");
                    json.append("\"difficulty\":\"").append(escapeJson(rs.getString("difficulty"))).append("\"");
                    json.append("}");
                    first = false;
                }
                
                json.append("]");
                
                rs.close();
                pstmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error\"}");
            }
        }
    }
    
    // API: Get meal plans
    static class MealPlansHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT mealplan_id, user_id, title, start_date, end_date " +
                              "FROM mealplan ORDER BY start_date DESC LIMIT 10";
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                
                while (rs.next()) {
                    if (!first) json.append(",");
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
                
                rs.close();
                stmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error\"}");
            }
        }
    }
    
    // API: Get ingredients
    static class IngredientsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT ingredient_id, name, unit, calories_per_unit, protein, fat, carbs " +
                              "FROM ingredient ORDER BY name LIMIT 50";
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                
                while (rs.next()) {
                    if (!first) json.append(",");
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
                
                rs.close();
                stmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error\"}");
            }
        }
    }
    
    // API: Get database stats
    static class StatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                
                StringBuilder json = new StringBuilder("{");
                
                // Count recipes
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM recipe");
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
                
                rs.close();
                stmt.close();
                
                sendJsonResponse(exchange, 200, json.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Database error\"}");
            }
        }
    }
    
    // Helper methods
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, json.length());
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}