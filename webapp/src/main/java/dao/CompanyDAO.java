package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// Add other necessary imports

import dto.Company;

public class CompanyDAO {
    // Database connection details (consider centralizing later)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/webapp2?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9&rewriteBatchedStatements=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public CompanyDAO() {
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
    // public Company getCompanyById(int companyId) { /* ... */ }
    // public Company findCompanyByName(String companyName) { /* ... */ }
    // public Company insertCompany(Company company) { /* ... */ } // returning Company with ID

    public Company getCompanyById(int companyId) {
        String sql = "SELECT company_id, company_name, zipcode, address, phone FROM company WHERE company_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Company company = new Company();
                    company.setCompany_id(rs.getInt("company_id"));
                    company.setCompany_name(rs.getString("company_name"));
                    company.setZipcode(rs.getString("zipcode"));
                    company.setAddress(rs.getString("address"));
                    company.setPhone(rs.getString("phone"));
                    return company;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Company findCompanyByName(String companyName) {
        String sql = "SELECT company_id, company_name, zipcode, address, phone FROM company WHERE company_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, companyName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Company company = new Company();
                    company.setCompany_id(rs.getInt("company_id"));
                    company.setCompany_name(rs.getString("company_name"));
                    company.setZipcode(rs.getString("zipcode"));
                    company.setAddress(rs.getString("address"));
                    company.setPhone(rs.getString("phone"));
                    return company;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return null;
    }

    public Company insertCompany(Company company) {
        String sql = "INSERT INTO company (company_name, zipcode, address, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, company.getCompany_name());
            pstmt.setString(2, company.getZipcode());
            pstmt.setString(3, company.getAddress());
            pstmt.setString(4, company.getPhone());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating company failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setCompany_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating company failed, no ID obtained.");
                }
            }
            return company;
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
            return null; // Or throw custom exception
        }
    }
}
