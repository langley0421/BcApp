package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;
import dto.Card;

@WebServlet("/recentlyServlet")
public class RecentlyServlet extends HttpServlet {

    private final CardDAO cardDAO = new CardDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // 日付フォーマット例

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String daysParam = req.getParameter("days");
        int days = 7; // デフォルト7日
        try {
            if (daysParam != null) {
                days = Integer.parseInt(daysParam);
            }
        } catch (NumberFormatException e) {
            // デフォルトの7日を使う
        }

        try (PrintWriter out = resp.getWriter()) {
            List<Card> allCards = cardDAO.findAll();

            long nowMillis = System.currentTimeMillis();
            long millisLimit = days * 24L * 60 * 60 * 1000;

            List<Card> filtered = allCards.stream()
                    .filter(card -> {
                        String createdDateStr = card.getCreatedDate();
                        if (createdDateStr == null || createdDateStr.isEmpty()) return false;
                        try {
                            // 文字列の日付をミリ秒に変換 (java.util.Dateのparseはdeprecatedなので手動)
                            // ISO 8601なら、java.timeで処理する方法もありますがJava 8以上なら
                            java.time.ZonedDateTime zdt = java.time.ZonedDateTime.parse(createdDateStr);
                            long createdMillis = zdt.toInstant().toEpochMilli();
                            return (nowMillis - createdMillis) <= millisLimit;
                        } catch (Exception ex) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            // JSON配列の手動生成
            out.print("[");
            for (int i = 0; i < filtered.size(); i++) {
                Card c = filtered.get(i);
                out.print("{");
                out.printf("\"cardId\":%d,", c.getCardId());
                out.printf("\"name\":\"%s\",", escapeJson(c.getName()));
                out.printf("\"email\":\"%s\",", escapeJson(c.getEmail()));
                out.printf("\"remarks\":\"%s\",", escapeJson(c.getRemarks()));
                out.printf("\"favorite\":%s,", c.isFavorite());
                out.printf("\"companyName\":\"%s\",", escapeJson(c.getCompanyName()));
                out.printf("\"companyZipcode\":\"%s\",", escapeJson(c.getCompanyZipcode()));
                out.printf("\"companyAddress\":\"%s\",", escapeJson(c.getCompanyAddress()));
                out.printf("\"companyPhone\":\"%s\",", escapeJson(c.getCompanyPhone()));
                out.printf("\"departmentName\":\"%s\",", escapeJson(c.getDepartmentName()));
                out.printf("\"positionName\":\"%s\",", escapeJson(c.getPositionName()));
                out.printf("\"created_date\":\"%s\"", escapeJson(c.getCreatedDate()));
                out.print("}");
                if (i < filtered.size() - 1) out.print(",");
            }
            out.print("]");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error occurred\"}");
            e.printStackTrace();
        }
    }

    // JSON用に文字列をエスケープ（最低限）
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
