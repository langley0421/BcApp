package servlet;

<<<<<<< HEAD
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation (replace with DAO call in a real application)
        if ("test@example.com".equals(email) && "password".equals(password)) {
            // Successful login
            HttpSession session = request.getSession();
            session.setAttribute("username", email); // Using email as username for now
            response.sendRedirect("home"); // Redirect to HomeServlet
        } else {
            // Failed login
            request.setAttribute("error", "Invalid email or password.");
=======
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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
>>>>>>> branch 'feature/jsp-servlet-conversion-auth' of https://github.com/langley0421/BcApp.git
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
