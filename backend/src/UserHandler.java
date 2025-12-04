import java.io.*;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import com.sun.net.httpserver.*;

public class UserHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException {
        WebServer.setCorsHeaders(exchange);

        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if ("POST".equalsIgnoreCase(requestMethod)) {
            handlePost(exchange, path);
        } else {
            WebServer.sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private void handlePost(HttpExchange exchange, String path) throws IOException {
        String requestBody;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody()))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        if (path.contains("/login")) {
            handleLogin(exchange, requestBody);
        } else if (path.contains("/signup")) {
            handleSignup(exchange, requestBody);
        } else if (path.contains("/change-password")) {
            handlePasswordChange(exchange, requestBody);
        } else {
            WebServer.sendJsonResponse(exchange, 404, "{\"error\":\"Endpoint not found\"}");
        }
    }

    private void handleLogin(HttpExchange exchange, String requestBody) throws IOException {
        Map<String, String> data = WebServer.parseJson(requestBody);
        String email = data.get("email");
        String password = data.get("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"Missing email or password\"}");
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

                String response = String.format(
                        "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"message\":\"Login successful\"}",
                        userId,
                        WebServer.escapeJson(userName),
                        WebServer.escapeJson(userEmail),
                        WebServer.escapeJson(dietType != null ? dietType : "none"));
                WebServer.sendJsonResponse(exchange, 200, response);
            } else {
                WebServer.sendJsonResponse(exchange, 401, "{\"error\":\"Invalid email or password\"}");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error during login\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }

    private void handleSignup(HttpExchange exchange, String requestBody) throws IOException {
        Map<String, String> data = WebServer.parseJson(requestBody);
        String name = data.get("name");
        String email = data.get("email");
        String password = data.get("password");
        String dietType = data.get("diet_type");

        if (name == null || email == null || password == null || dietType == null ||
                name.isEmpty() || email.isEmpty() || password.isEmpty() || dietType.isEmpty()) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"All fields are required\"}");
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
                WebServer.sendJsonResponse(exchange, 409, "{\"error\":\"Email already registered\"}");
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
                    String response = String.format(
                            "{\"userId\":%d,\"name\":\"%s\",\"email\":\"%s\",\"diet_type\":\"%s\",\"message\":\"Registration successful\"}",
                            userId,
                            WebServer.escapeJson(name),
                            WebServer.escapeJson(email),
                            WebServer.escapeJson(dietType));
                    WebServer.sendJsonResponse(exchange, 201, response);
                } else {
                    WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to get user ID\"}");
                }
            } else {
                WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Registration failed\"}");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            if (e.getMessage().contains("Duplicate entry")) {
                WebServer.sendJsonResponse(exchange, 409, "{\"error\":\"Email already registered\"}");
            } else {
                WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error during registration\"}");
            }
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }

    private void handlePasswordChange(HttpExchange exchange, String requestBody) throws IOException {
        Map<String, String> data = WebServer.parseJson(requestBody);
        String userId = data.get("userId");
        String currentPassword = data.get("currentPassword");
        String newPassword = data.get("newPassword");

        if (userId == null || currentPassword == null || newPassword == null ||
                userId.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            WebServer.sendJsonResponse(exchange, 400, "{\"error\":\"All fields are required\"}");
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
                WebServer.sendJsonResponse(exchange, 401, "{\"error\":\"Current password is incorrect\"}");
                return;
            }
            pstmt.close();

            String updateQuery = "UPDATE user SET password = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, Integer.parseInt(userId));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                WebServer.sendJsonResponse(exchange, 200, "{\"message\":\"Password changed successfully\"}");
            } else {
                WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Failed to change password\"}");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            WebServer.sendJsonResponse(exchange, 500, "{\"error\":\"Database error\"}");
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
