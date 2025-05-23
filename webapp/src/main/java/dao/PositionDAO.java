package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// Add other necessary imports

import dto.Position;

public class PositionDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/webapp2?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9&rewriteBatchedStatements=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public PositionDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
    
    // TODO: Implement methods:
    // public Position getPositionById(int positionId) { /* ... */ }
    // public Position findPositionByName(String positionName) { /* ... */ }
    // public Position insertPosition(Position position) { /* ... */ } // returning Position with ID

    public Position getPositionById(int positionId) {
        String sql = "SELECT position_id, position_name FROM position WHERE position_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, positionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Position pos = new Position();
                    pos.setPosition_id(rs.getInt("position_id"));
                    pos.setPosition_name(rs.getString("position_name"));
                    return pos;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Position findPositionByName(String positionName) {
        String sql = "SELECT position_id, position_name FROM position WHERE position_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, positionName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Position pos = new Position();
                    pos.setPosition_id(rs.getInt("position_id"));
                    pos.setPosition_name(rs.getString("position_name"));
                    return pos;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Position insertPosition(Position position) {
        String sql = "INSERT INTO position (position_name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, position.getPosition_name());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating position failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    position.setPosition_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating position failed, no ID obtained.");
                }
            }
            return position;
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
            return null; // Or throw custom exception
        }
    }
}
