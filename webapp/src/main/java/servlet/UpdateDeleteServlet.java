package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BcDAO;
import dto.Bc;
import dto.Result;

/**
 * Servlet implementation class UpdateDeleteServlet
 */
@WebServlet("/UpdateDeleteServlet")
public class UpdateDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// もしもログインしていなかったらログインサーブレットにリダイレクトする
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/webapp/LoginServlet");
			return;
		}

		// リクエストパラメータを取得する
		request.setCharacterEncoding("UTF-8");
		int number = Integer.parseInt(request.getParameter("number"));
		String name = request.getParameter("name");
		String address = request.getParameter("address");

		// 更新または削除を行う
		BcDAO bDao = new BcDAO();
		if (request.getParameter("submit").equals("更新")) {
			if (bDao.update(new Bc(number, name, address))) { // 更新成功
				request.setAttribute("result", new Result("更新成功！", "レコードを更新しました。", "/webapp/MenuServlet"));
			} else { // 更新失敗
				request.setAttribute("result", new Result("更新失敗！", "レコードを更新できませんでした。", "/webapp/MenuServlet"));
			}
		} else {
			if (bDao.delete(new Bc(number, name, address))) { // 削除成功
				request.setAttribute("result", new Result("削除成功！", "レコードを削除しました。", "/webapp/MenuServlet"));
			} else { // 削除失敗
				request.setAttribute("result", new Result("削除失敗！", "レコードを削除できませんでした。", "/webapp/MenuServlet"));
			}
		}

		// 結果ページにフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/result.jsp");
		dispatcher.forward(request, response);
	}
}
