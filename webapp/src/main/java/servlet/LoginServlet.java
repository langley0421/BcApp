package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 追加のインポート
import dao.UserDAO;
import dto.User;
import util.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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

        User user = userDAO.findUserByEmail(email);

        // PasswordUtil.checkPasswordからsalt引数を削除
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            // ログイン成功
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getEmail()); // ユーザー名としてemailを使用
            response.sendRedirect("home"); // HomeServletへリダイレクト
        } else {
            // ログイン失敗
            request.setAttribute("error", "メールアドレスまたはパスワードが正しくありません。");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}
