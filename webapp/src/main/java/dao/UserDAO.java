package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.User;

public class UserDAO {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_cardApp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // ユーザー登録処理
    public boolean createUser(User user) {
        String sql = "INSERT INTO user (email, password) VALUES (?, ?)";

        try {
            // JDBCドライバを読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getPassword()); // ここはハッシュ化済みのパスワードを渡す
                stmt.executeUpdate();
                return true;

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBCドライバの読み込みに失敗しました");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLエラーが発生しました");
            return false;
        }
    }

    // メールアドレスでユーザーを検索（ログイン処理用）
    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    User user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password")); // ハッシュ化されたパスワード
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
