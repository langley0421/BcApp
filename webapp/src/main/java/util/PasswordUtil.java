package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
// SecureRandom is no longer needed as salt is removed
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordUtil {

    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * 与えられたパスワードをハッシュ化します。
     * @param plainPassword ハッシュ化する平文のパスワード
     * @return Base64エンコードされたハッシュ化パスワード。エラー時はnull。
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            LOGGER.warning("Password cannot be null for hashing.");
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashedPassword = md.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Failed to hash password, algorithm not found: " + HASH_ALGORITHM, e);
            return null;
        }
    }

    /**
     * 提供された平文パスワードが、保存されているハッシュ化パスワードと一致するか検証します。
     * @param plainPassword 検証する平文のパスワード
     * @param hashedPasswordFromDB データベースに保存されているBase64エンコードされたハッシュ化パスワード
     * @return パスワードが一致すればtrue、そうでなければfalse
     */
    public static boolean checkPassword(String plainPassword, String hashedPasswordFromDB) {
        if (plainPassword == null || hashedPasswordFromDB == null) {
            LOGGER.warning("Plain password or stored hashed password cannot be null for checking.");
            return false;
        }
        String newHash = hashPassword(plainPassword);
        if (newHash == null) {
            return false; // ハッシュ化に失敗
        }
        return newHash.equals(hashedPasswordFromDB);
    }

    // 簡単なテスト用mainメソッド (本番では削除またはコメントアウト)
    public static void main(String[] args) {
        String password = "password123";

        String hashedPassword = hashPassword(password);
        System.out.println("ハッシュ化パスワード: " + hashedPassword);

        boolean isMatch = checkPassword(password, hashedPassword);
        System.out.println("パスワード一致 (初回): " + isMatch);

        boolean isNotMatch = checkPassword("wrongpassword", hashedPassword);
        System.out.println("パスワード不一致: " + isNotMatch);
        
        // Nullチェックのテスト
        System.out.println("Nullチェック (hashedPasswordFromDBがnull): " + checkPassword(password, null));
    }
}
