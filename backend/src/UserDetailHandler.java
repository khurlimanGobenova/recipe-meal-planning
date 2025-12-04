import java.io.*;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class UserDetailHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        String[] parts = path.split("/");
        if (parts.length < 4) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid URL\"}");
            return;
        }

        String userIdStr = parts[3];
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Invalid user ID\"}");
            return;
        }

        if ("GET".equalsIgnoreCase(requestMethod)) {
            handleGet(exchange, userId);
        } else if ("PUT".equalsIgnoreCase(requestMethod)) {
            handlePut(exchange, userId);
        } else {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private void handleGet(HttpExchange exchange, int userId) throws IOException {
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
                        WebServer.escapeJson(rs.getString("name")),
                        WebServer.escapeJson(rs.getString("email")),
                        WebServer.escapeJson(rs.getString("diet_type") != null ? rs.getString("diet_type") : "none"),
                        rs.getTimestamp("created_at").toString());
                WebServer.sendJsonResponse(exchange, 200, response);
            } else {
                WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"User not found\"}");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }

    private void handlePut(HttpExchange exchange, int userId) throws IOException {
        String requestBody;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody()))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        Map<String, String> data = WebServer.parseJson(requestBody);
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
                        WebServer.escapeJson(name),
                        WebServer.escapeJson(email),
                        WebServer.escapeJson(dietType));
                WebServer.sendJsonResponse(exchange, 200, response);
            } else {
                WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"User not found\"}");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, null);
        }
    }
}
