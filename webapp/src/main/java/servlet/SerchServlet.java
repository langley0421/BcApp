package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;

@WebServlet("/card")
public class SerchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchTerm = request.getParameter("searchTerm");

        if ("list".equals(action)) {
            CardDAO dao = new CardDAO();
            List<Card> cards;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                cards = dao.searchCardsByKeyword(searchTerm);
            } else {
                cards = dao.findAll();
            }

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new Gson().toJson(cards));
        }
    }
}
