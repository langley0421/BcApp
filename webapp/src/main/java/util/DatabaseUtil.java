package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // これらの値は一般的なプレースホルダーです。
    // ご自身の環境に合わせて設定を変更する必要がある可能性が高いです。
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String JDBC_USER = "your_db_user";
    private static final String JDBC_PASSWORD = "your_db_password";

    static {
        try {
            // MySQL Connector/J 8.x のドライバを想定しています。
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found. WEB-INF/lib に com.mysql.cj.jdbc.Driver を含むJARファイルがあるか確認してください。", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
    }
}
