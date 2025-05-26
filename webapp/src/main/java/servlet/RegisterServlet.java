package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 追加のインポート
import dao.UserDAO;
import dto.User;
import util.PasswordUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 基本的な検証
        if (password != null && password.equals(confirmPassword)) {
            // パスワードが一致する場合
            // String salt = PasswordUtil.generateSalt(); // salt生成を削除
            String hashedPassword = PasswordUtil.hashPassword(password); // saltなしでハッシュ化

            if (hashedPassword == null) {
                // ハッシュ化失敗
                request.setAttribute("error", "登録処理中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(hashedPassword); // ハッシュ化されたパスワードを設定
            // user.setSalt(salt); // saltの設定を削除

            boolean success = userDAO.createUser(user);

            if (success) {
                // 登録成功
                request.setAttribute("message", "ユーザー登録が成功しました。ログインしてください。");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            } else {
                // 登録失敗 (メールアドレス重複またはDBエラー)
                request.setAttribute("error", "このメールアドレスは既に使用されているか、登録中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
            }
        } else {
            // 登録失敗 - パスワードが一致しません
            request.setAttribute("error", "パスワードが一致しません。");
            request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
        }
    }
}
