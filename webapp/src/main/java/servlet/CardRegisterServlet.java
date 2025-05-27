package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;

@WebServlet("/cardRegister")
public class CardRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // フォームからの値取得
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

        // DAOで登録処理
        CardDAO dao = new CardDAO();
        dao.insertCard(companyName, companyZipcode, companyAddress, companyPhone,
                       departmentName, positionName, name, email, remarks, favorite);

        // 登録後、一覧へリダイレクト
        response.sendRedirect(request.getContextPath() + "/home");

    }
}
