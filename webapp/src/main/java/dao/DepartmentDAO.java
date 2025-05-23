package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// Add other necessary imports

import dto.Department;

public class DepartmentDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/webapp2?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9&rewriteBatchedStatements=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    public DepartmentDAO() {
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
    // public Department getDepartmentById(int departmentId) { /* ... */ }
    // public Department findDepartmentByName(String departmentName) { /* ... */ }
    // public Department insertDepartment(Department department) { /* ... */ } // returning Department with ID

    public Department getDepartmentById(int departmentId) {
        String sql = "SELECT department_id, department_name FROM department WHERE department_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, departmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Department dept = new Department();
                    dept.setDepartment_id(rs.getInt("department_id"));
                    dept.setDepartment_name(rs.getString("department_name"));
                    return dept;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Department findDepartmentByName(String departmentName) {
        String sql = "SELECT department_id, department_name FROM department WHERE department_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, departmentName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Department dept = new Department();
                    dept.setDepartment_id(rs.getInt("department_id"));
                    dept.setDepartment_name(rs.getString("department_name"));
                    return dept;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Department insertDepartment(Department department) {
        String sql = "INSERT INTO department (department_name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, department.getDepartment_name());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating department failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setDepartment_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating department failed, no ID obtained.");
                }
            }
            return department;
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
            return null; // Or throw custom exception
        }
    }
}
