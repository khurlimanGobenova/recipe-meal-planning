import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class WebServer {

    private static final int PORT = 8080;
    private static final String CORS_ORIGIN = "*";

    public static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", CORS_ORIGIN);
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/users/login", new UserHandler());
        server.createContext("/api/users/signup", new UserHandler());
        server.createContext("/api/users/change-password", new UserHandler());
        server.createContext("/api/users", new UserHandler());
        server.createContext("/api/users/", new UserDetailHandler());

        server.createContext("/api/recipes", new RecipeHandler());
        server.createContext("/api/recipes/", new RecipeDetailHandler());

        server.createContext("/api/search", new SearchHandler());

        server.createContext("/api/mealplans", new MealPlanHandler());
        server.createContext("/api/mealentries", new MealEntryHandler());
        server.createContext("/api/shopping-list", new ShoppingListHandler());
        server.createContext("/api/nutrition-progress", new NutritionProgressHandler());

        server.createContext("/api/ingredients", new IngredientsHandler());
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/health-goals", new HealthGoalsHandler());

        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("===================================");
        System.out.println("  HEAL MEAL WEB SERVER STARTED");
        System.out.println("===================================");
        System.out.println("Server running at: http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop the server");
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
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg")) return "image/jpeg";
            return "text/plain";
        }
    }

    public static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    public static Map<String, String> parseJson(String json) {
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

    public static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}