package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CardDAO;
import dto.Card;

@WebServlet("/cardEdit")
public class CardEditServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // フォームからの値を取得
        int cardId = Integer.parseInt(request.getParameter("card_id"));
        String companyName = request.getParameter("company_name");
        String companyZipcode = request.getParameter("company_zipcode");
        String companyAddress = request.getParameter("company_address");
        String companyPhone = request.getParameter("company_phone");

        String departmentName = request.getParameter("department_name");
        String positionName = request.getParameter("position_name");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String remarks = request.getParameter("remarks");
        boolean favorite = request.getParameter("favorite") != null;

        // Cardオブジェクトに詰める
        Card card = new Card();
        card.setCardId(cardId);
        card.setCompanyName(companyName);
        card.setCompanyZipcode(companyZipcode);
        card.setCompanyAddress(companyAddress);
        card.setCompanyPhone(companyPhone);
        card.setDepartmentName(departmentName);
        card.setPositionName(positionName);
        card.setName(name);
        card.setEmail(email);
        card.setRemarks(remarks);
        card.setFavorite(favorite);

        // DAOを使って更新処理
        CardDAO dao = new CardDAO();
        boolean updateSuccess = dao.updateCard(card);

        HttpSession session = request.getSession();
        if (updateSuccess) {
            session.setAttribute("message", "カードが更新されました。");
        } else {
            session.setAttribute("error", "カードの更新中にエラーが発生しました。");
        }

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
