package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // パスワードをハッシュ化（SHA-256）
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            // バイト配列を16進数文字列に変換
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 入力されたパスワードが保存されたハッシュと一致するか確認
    public static boolean checkPassword(String inputPassword, String storedHashedPassword) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput != null && hashedInput.equals(storedHashedPassword);
    }
}
