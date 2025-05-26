package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Basic validation
        if (password != null && password.equals(confirmPassword)) {
            // Successful registration (in a real app, save user to DB here)
            request.setAttribute("message", "Registration successful. Please login.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            // Failed registration - passwords do not match
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/jsp/regist.jsp").forward(request, response);
        }
    }
}
