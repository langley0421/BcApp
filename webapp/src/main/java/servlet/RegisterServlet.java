package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字化け防止（リクエストとレスポンスのエンコーディング設定）
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (password != null && password.equals(confirmPassword)) {
            // パスワード一致時
            String hashedPassword = PasswordUtil.hashPassword(password);

            if (hashedPassword == null) {
                request.setAttribute("error", "登録処理中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(hashedPassword);

            boolean success = userDAO.createUser(user);

            if (success) {
                request.setAttribute("message", "ユーザー登録が成功しました。ログインしてください。");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "このメールアドレスは既に使用されているか、登録中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
            }
        } else {
            // パスワード不一致時
            request.setAttribute("error", "パスワードが一致しません。");
            request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GETでも文字化けしないように設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}
