package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
// Add other necessary imports, including java.sql.Timestamp
import java.util.List;

import dto.CardInfo;
import dto.CardInput;

public class CardDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_cardApp?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9&rewriteBatchedStatements=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public CardDAO() {
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
    // public List<CardInfo> getCardList(String searchTerm, Boolean favoriteFilter, String sortBy) { /* ... */ }
    // public CardInfo getCardById(int cardId) { /* ... */ }
    // public int insertCard(CardInput cardInput, int companyId, int departmentId, int positionId) { /* ... */ } // returns generated card_id
    // public boolean updateCard(CardInput cardInput, int companyId, int departmentId, int positionId) { /* ... */ }
    // public boolean updateFavoriteStatus(int cardId, boolean isFavorite) { /* ... */ }
    // public boolean deleteCard(int cardId) { /* ... */ }

    public List<CardInfo> getCardList(String searchTerm, Boolean favoriteFilter, String sortBy) {
        List<CardInfo> cardList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT cr.card_id, cr.name, cr.email, cr.remarks, cr.favorite, cr.created_date, cr.update_date, " +
            "co.company_id, co.company_name, co.zipcode AS company_zipcode, co.address AS company_address, co.phone AS company_phone, " +
            "d.department_id, d.department_name, p.position_id, p.position_name " +
            "FROM card cr " +
            "JOIN company co ON cr.company_id = co.company_id " +
            "JOIN department d ON cr.department_id = d.department_id " +
            "JOIN position p ON cr.position_id = p.position_id"
        );

        List<Object> params = new ArrayList<>();
        boolean whereClauseAdded = false;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" WHERE (cr.name LIKE ? OR co.company_name LIKE ? OR d.department_name LIKE ? OR p.position_name LIKE ? OR cr.remarks LIKE ?)");
            String likeSearchTerm = "%" + searchTerm + "%";
            for (int i = 0; i < 5; i++) {
                params.add(likeSearchTerm);
            }
            whereClauseAdded = true;
        }

        if (favoriteFilter != null) {
            sql.append(whereClauseAdded ? " AND" : " WHERE").append(" cr.favorite = ?");
            params.add(favoriteFilter);
        }

        if ("name".equals(sortBy)) {
            sql.append(" ORDER BY cr.name ASC");
        } else if ("company".equals(sortBy)) {
            sql.append(" ORDER BY co.company_name ASC");
        } else { // Default or "date"
            sql.append(" ORDER BY cr.created_date DESC");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CardInfo card = new CardInfo(
                        rs.getInt("card_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("remarks"),
                        rs.getBoolean("favorite"),
                        rs.getTimestamp("created_date"),
                        rs.getTimestamp("update_date"),
                        rs.getString("company_name"),
                        rs.getString("company_zipcode"),
                        rs.getString("company_address"),
                        rs.getString("company_phone"),
                        rs.getString("department_name"),
                        rs.getString("position_name"),
                        rs.getInt("company_id"),
                        rs.getInt("department_id"),
                        rs.getInt("position_id")
                    );
                    cardList.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
        }
        return cardList;
    }

    public CardInfo getCardById(int cardId) {
        String sql = "SELECT cr.card_id, cr.name, cr.email, cr.remarks, cr.favorite, cr.created_date, cr.update_date, " +
                     "co.company_id, co.company_name, co.zipcode AS company_zipcode, co.address AS company_address, co.phone AS company_phone, " +
                     "d.department_id, d.department_name, p.position_id, p.position_name " +
                     "FROM card cr " +
                     "JOIN company co ON cr.company_id = co.company_id " +
                     "JOIN department d ON cr.department_id = d.department_id " +
                     "JOIN position p ON cr.position_id = p.position_id " +
                     "WHERE cr.card_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CardInfo(
                        rs.getInt("card_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("remarks"),
                        rs.getBoolean("favorite"),
                        rs.getTimestamp("created_date"),
                        rs.getTimestamp("update_date"),
                        rs.getString("company_name"),
                        rs.getString("company_zipcode"),
                        rs.getString("company_address"),
                        rs.getString("company_phone"),
                        rs.getString("department_name"),
                        rs.getString("position_name"),
                        rs.getInt("company_id"),
                        rs.getInt("department_id"),
                        rs.getInt("position_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
        }
        return null;
    }

    public int insertCard(CardInput cardInput, int companyId, int departmentId, int positionId) {
        String sql = "INSERT INTO card (name, email, remarks, favorite, company_id, department_id, position_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cardInput.getName());
            pstmt.setString(2, cardInput.getEmail());
            pstmt.setString(3, cardInput.getRemarks());
            pstmt.setBoolean(4, cardInput.isFavorite());
            pstmt.setInt(5, companyId);
            pstmt.setInt(6, departmentId);
            pstmt.setInt(7, positionId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating card failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
            return -1; // Or throw custom exception
        }
    }

    public boolean updateCard(CardInput cardInput, int companyId, int departmentId, int positionId) {
        String sql = "UPDATE card SET name = ?, email = ?, remarks = ?, favorite = ?, company_id = ?, department_id = ?, position_id = ? WHERE card_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardInput.getName());
            pstmt.setString(2, cardInput.getEmail());
            pstmt.setString(3, cardInput.getRemarks());
            pstmt.setBoolean(4, cardInput.isFavorite());
            pstmt.setInt(5, companyId);
            pstmt.setInt(6, departmentId);
            pstmt.setInt(7, positionId);
            pstmt.setInt(8, cardInput.getCard_id());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
            return false;
        }
    }

    public boolean updateFavoriteStatus(int cardId, boolean isFavorite) {
        String sql = "UPDATE card SET favorite = ? WHERE card_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isFavorite);
            pstmt.setInt(2, cardId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
            return false;
        }
    }

    public boolean deleteCard(int cardId) {
        String sql = "DELETE FROM card WHERE card_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cardId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle error appropriately
            return false;
        }
    }
}
