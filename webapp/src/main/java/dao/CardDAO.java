package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Card;

public class CardDAO {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found");
        }
        String url = "jdbc:mysql://localhost:3306/db_cardApp";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }

    public List<Card> findAll() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT \r\n"
                + "    c.card_id,\r\n"
                + "    c.name,\r\n"
                + "    c.email,\r\n"
                + "    c.remarks,\r\n"
                + "    c.favorite,\r\n"
                + "    com.company_name,\r\n"
                + "    com.zipcode,\r\n"
                + "    com.address,\r\n"
                + "    com.phone,\r\n"
                + "    d.department_name,\r\n"
                + "    p.position_name,\r\n"
                + "    c.created_date\r\n"
                + "FROM card c\r\n"
                + "JOIN company com ON c.company_id = com.company_id\r\n"
                + "JOIN department d ON c.department_id = d.department_id\r\n"
                + "JOIN position p ON c.position_id = p.position_id;";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Card card = new Card();
                card.setCardId(rs.getInt("card_id"));
                card.setCompanyName(rs.getString("company_name"));
                card.setCompanyZipcode(rs.getString("zipcode"));
                card.setCompanyAddress(rs.getString("address"));
                card.setCompanyPhone(rs.getString("phone"));
                card.setName(rs.getString("name"));
                card.setEmail(rs.getString("email"));
                card.setRemarks(rs.getString("remarks"));
                card.setFavorite(rs.getBoolean("favorite"));
                card.setDepartmentName(rs.getString("department_name"));
                card.setPositionName(rs.getString("position_name"));
                card.setCreatedDate(rs.getString("created_date"));

                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    public boolean insertCard(String companyName, String zipcode, String address, String phone,
                           String departmentName, String positionName,
                           String name, String email, String remarks, boolean favorite) {
        boolean success = false;
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            int companyId = getOrInsertCompany(conn, companyName, zipcode, address, phone);
            int departmentId = getOrInsertDepartment(conn, departmentName);
            int positionId = getOrInsertPosition(conn, positionName);

            String sql = "INSERT INTO card (company_id, department_id, position_id, name, email, remarks, favorite) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, companyId);
                stmt.setInt(2, departmentId);
                stmt.setInt(3, positionId);
                stmt.setString(4, name);
                stmt.setString(5, email);
                stmt.setString(6, remarks);
                stmt.setBoolean(7, favorite);
                stmt.executeUpdate();
            }

            conn.commit();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            // success remains false
        }
        return success;
    }

    private int getOrInsertCompany(Connection conn, String name, String zipcode, String address, String phone) throws SQLException {
        String select = "SELECT company_id FROM company WHERE company_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("company_id");
            }
        }

        String insert = "INSERT INTO company (company_name, zipcode, address, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, zipcode);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert company");
    }

    private int getOrInsertDepartment(Connection conn, String name) throws SQLException {
        String select = "SELECT department_id FROM department WHERE department_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("department_id");
            }
        }

        String insert = "INSERT INTO department (department_name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert department");
    }

    private int getOrInsertPosition(Connection conn, String name) throws SQLException {
        String select = "SELECT position_id FROM position WHERE position_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("position_id");
            }
        }

        String insert = "INSERT INTO position (position_name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert position");
    }
    
    public boolean deleteCardById(int cardId) {
        String sql = "DELETE FROM card WHERE card_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cardId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // CardDAO.java に追加
    public boolean updateCard(Card card) {
        String sql = "UPDATE cards SET company_name = ?, company_zipcode = ?, company_address = ?, " +
                     "company_phone = ?, name = ?, email = ?, remarks = ?, favorite = ?, " +
                     "department_name = ?, position_name = ? WHERE card_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, card.getCompanyName());
            stmt.setString(2, card.getCompanyZipcode());
            stmt.setString(3, card.getCompanyAddress());
            stmt.setString(4, card.getCompanyPhone());
            stmt.setString(5, card.getName());
            stmt.setString(6, card.getEmail());
            stmt.setString(7, card.getRemarks());
            stmt.setBoolean(8, card.isFavorite());
            stmt.setString(9, card.getDepartmentName());
            stmt.setString(10, card.getPositionName());
            stmt.setInt(11, card.getCardId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
