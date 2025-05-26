package dao;

import dto.User;
import util.DatabaseUtil; // DatabaseUtilをutilパッケージからインポート
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Statement import might not be needed if not using generated keys explicitly here
// import java.sql.Statement; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public User findUserByEmail(String email) {
        String sql = "SELECT id, email, password FROM user WHERE email = ?"; // saltカラムの取得を削除
        User user = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password")); // これはハッシュ化されたパスワード
                    // user.setSalt(rs.getString("salt")); // saltの設定を削除
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "メールによるユーザー検索エラー: " + email, e);
        }
        return user;
    }

    public boolean createUser(User user) {
        // user.getPassword()は既にハッシュ化されていると仮定
        String sql = "INSERT INTO user (email, password) VALUES (?, ?)"; // saltカラムへの挿入を削除
        boolean success = false;

        if (user.getEmail() == null || user.getPassword() == null) { // saltのnullチェックを削除
            LOGGER.warning("ユーザーのメールまたはパスワードがnullです。ユーザーを作成できません。");
            return false;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            // stmt.setString(3, user.getSalt()); // saltの設定を削除

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;

        } catch (SQLException e) {
            // 一意性制約違反（メールアドレスが既に存在する）を確認
            if (e.getSQLState().startsWith("23")) { // 整合性制約違反のSQLState
                 LOGGER.log(Level.WARNING, "ユーザーを作成できませんでした。メール '" + user.getEmail() + "' は既に存在する可能性があります。", e);
            } else {
                LOGGER.log(Level.SEVERE, "ユーザー作成エラー: " + user.getEmail(), e);
            }
            success = false;
        }
        return success;
    }
}
