package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;
import dto.Card;

@WebServlet("/cardEdit")
public class CardEditServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 入力値取得
            int cardId = Integer.parseInt(request.getParameter("card_id"));
            String companyName = request.getParameter("company_name");
            String companyZipcode = request.getParameter("company_zipcode");
            String companyAddress = request.getParameter("company_address");
            String companyPhone = request.getParameter("company_phone");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String remarks = request.getParameter("remarks");
            String departmentName = request.getParameter("department_name");
            String positionName = request.getParameter("position_name");
            boolean favorite = request.getParameter("favorite") != null;

            // DTOにセット
            Card card = new Card();
            card.setCardId(cardId);
            card.setCompanyName(companyName);
            card.setCompanyZipcode(companyZipcode);
            card.setCompanyAddress(companyAddress);
            card.setCompanyPhone(companyPhone);
            card.setName(name);
            card.setEmail(email);
            card.setRemarks(remarks);
            card.setDepartmentName(departmentName);
            card.setPositionName(positionName);
            card.setFavorite(favorite);

            // DB更新
            CardDAO dao = new CardDAO();
            boolean success = dao.updateCard(card);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/index.jsp"); // 編集後トップへ
            } else {
                request.setAttribute("error", "編集に失敗しました。");
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "データの処理中にエラーが発生しました。");
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        }
    }
}
