package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;
import dto.Card;

@WebServlet("/cardServlet")
public class CardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CardDAO dao = new CardDAO();
        List<Card> cards = dao.findAll();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            json.append("{");
            json.append("\"cardId\":").append(card.getCardId()).append(",");
            json.append("\"companyName\":\"").append(escape(card.getCompanyName())).append("\",");
            json.append("\"companyZipcode\":\"").append(escape(card.getCompanyZipcode())).append("\",");
            json.append("\"companyAddress\":\"").append(escape(card.getCompanyAddress())).append("\",");
            json.append("\"companyPhone\":\"").append(escape(card.getCompanyPhone())).append("\",");
            json.append("\"name\":\"").append(escape(card.getName())).append("\",");
            json.append("\"email\":\"").append(escape(card.getEmail())).append("\",");
            json.append("\"remarks\":\"").append(escape(card.getRemarks())).append("\",");
            json.append("\"favorite\":").append(card.isFavorite()).append(",");
            json.append("\"departmentName\":\"").append(escape(card.getDepartmentName())).append("\",");
            json.append("\"positionName\":\"").append(escape(card.getPositionName())).append("\",");
            json.append("\"createdDate\":\"").append(escape(card.getCreatedDate())).append("\"");
            json.append("}");

            if (i < cards.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        out.print(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String cardIdStr = req.getParameter("cardId");
            int cardId = Integer.parseInt(cardIdStr);

            CardDAO dao = new CardDAO();
            boolean success = dao.deleteCardById(cardId);

            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();

            if (success) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"削除に失敗しました。\"}");
            }
            out.flush();
        }
    }

    // JSON用の文字列エスケープ（最低限）
    private String escape(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
