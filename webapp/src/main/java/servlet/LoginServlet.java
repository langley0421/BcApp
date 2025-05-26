package servlet;

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
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
