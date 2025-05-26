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
            Class.forName("com.mysql.cj.jdbc.Driver");  // ここでドライバをロード
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
        		+ "    c.created_date\r\n"   // created_at は created_date に合わせました
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
                card.setCompanyZipcode(rs.getString("zipcode"));  // カラム名に合わせ修正
                card.setCompanyAddress(rs.getString("address"));
                card.setCompanyPhone(rs.getString("phone"));
                card.setName(rs.getString("name"));
                card.setEmail(rs.getString("email"));
                card.setRemarks(rs.getString("remarks"));
                card.setFavorite(rs.getBoolean("favorite"));
                card.setDepartmentName(rs.getString("department_name"));
                card.setPositionName(rs.getString("position_name"));
                card.setCreatedAt(rs.getString("created_date"));  // こちらもカラム名に合わせました

                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }
}
