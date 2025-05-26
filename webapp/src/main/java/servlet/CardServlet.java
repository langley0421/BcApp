package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper; // jacksonライブラリを使用

import dao.CardDAO;
import dto.Card;

@WebServlet("/cardServlet")
public class CardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CardDAO dao = new CardDAO();
        List<Card> cards = dao.findAll();

        // JSONに変換してレスポンスに書き込む
        resp.setContentType("application/json;charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), cards);
    }
}
