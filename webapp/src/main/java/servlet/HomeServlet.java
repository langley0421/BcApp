package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Do not create a new session if one doesn't exist

        if (session != null && session.getAttribute("username") != null) {
            // User is logged in, forward to home.jsp
            // In the future, fetch data using CardDAO and set request attributes here
            request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
        } else {
            // User is not logged in or session expired, redirect to login page
            // Redirecting to "login" (mapped to LoginServlet which can show login.jsp)
            // or directly to a context-relative login.jsp if it's not under WEB-INF.
            // Since login.jsp is under WEB-INF, redirecting to "login" servlet or a general login URL is better.
            response.sendRedirect("login");
        }
    }
}
