package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
// Add other necessary imports for JSON building if any, or just use String manipulation

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CardDAO;
import dao.CompanyDAO;
import dao.DepartmentDAO;
import dao.PositionDAO;
import dto.CardInfo;
import dto.CardInput;
import dto.Company;
import dto.Department;
import dto.Position;

@WebServlet("/cardServlet")
public class CardServlet extends HttpServlet {
    private CardDAO cardDAO;
    private CompanyDAO companyDAO;
    private DepartmentDAO departmentDAO;
    private PositionDAO positionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        cardDAO = new CardDAO();
        companyDAO = new CompanyDAO();
        departmentDAO = new DepartmentDAO();
        positionDAO = new PositionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String jsonResponse = "";

        try {
            if ("list".equals(action)) {
                String searchTerm = request.getParameter("searchTerm");
                String favoriteParam = request.getParameter("favorite");
                Boolean favoriteFilter = null;
                if (favoriteParam != null && !favoriteParam.isEmpty()) {
                    favoriteFilter = Boolean.parseBoolean(favoriteParam);
                }
                String sortBy = request.getParameter("sortBy");

                List<CardInfo> cards = cardDAO.getCardList(searchTerm, favoriteFilter, sortBy);
                jsonResponse = manuallyBuildJsonForCardInfoList(cards); // Implement this helper
            } else {
                jsonResponse = "{\"success\": false, \"message\": \"Unknown action: " + escapeJsonString(action) + "\"}";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            jsonResponse = "{\"success\": false, \"message\": \"Server error: " + escapeJsonString(e.getMessage()) + "\"}";
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.print(jsonResponse);
        out.flush();
    }

    private String manuallyBuildJsonForCardInfoList(List<CardInfo> cards) {
        if (cards == null) return "[]"; // Or error
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < cards.size(); i++) {
            CardInfo card = cards.get(i);
            sb.append("{");
            sb.append("\"card_id\":").append(card.getCard_id()).append(",");
            sb.append("\"name\":\"").append(escapeJsonString(card.getName())).append("\",");
            sb.append("\"email\":\"").append(escapeJsonString(card.getEmail())).append("\",");
            sb.append("\"remarks\":\"").append(escapeJsonString(card.getRemarks())).append("\",");
            sb.append("\"favorite\":").append(card.isFavorite()).append(",");
            // Handle null dates by outputting null, not "null" string
            sb.append("\"created_date\":").append(card.getCreated_date() != null ? "\"" + card.getCreated_date().toString() + "\"" : null).append(",");
            sb.append("\"update_date\":").append(card.getUpdate_date() != null ? "\"" + card.getUpdate_date().toString() + "\"" : null).append(",");
            sb.append("\"company_name\":\"").append(escapeJsonString(card.getCompany_name())).append("\",");
            sb.append("\"company_zipcode\":\"").append(escapeJsonString(card.getCompany_zipcode())).append("\",");
            sb.append("\"company_address\":\"").append(escapeJsonString(card.getCompany_address())).append("\",");
            sb.append("\"company_phone\":\"").append(escapeJsonString(card.getCompany_phone())).append("\",");
            sb.append("\"department_name\":\"").append(escapeJsonString(card.getDepartment_name())).append("\",");
            sb.append("\"position_name\":\"").append(escapeJsonString(card.getPosition_name())).append("\",");
            sb.append("\"company_id\":").append(card.getCompany_id()).append(",");
            sb.append("\"department_id\":").append(card.getDepartment_id()).append(",");
            sb.append("\"position_id\":").append(card.getPosition_id());
            sb.append("}");
            if (i < cards.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Basic JSON string escaping
    private String escapeJsonString(String str) {
        if (str == null) return ""; // Return empty string for null to avoid "null" in JSON string values
        // Simplified: just escapes quotes and backslashes. A proper library would be more robust.
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
    // doPost will be implemented in the next subtask

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsonResponse = "";

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    jsonResponse = handleAddCard(request);
                    break;
                case "update":
                    jsonResponse = handleUpdateCard(request);
                    break;
                case "delete":
                    jsonResponse = handleDeleteCard(request);
                    break;
                case "toggleFavorite":
                    jsonResponse = handleToggleFavorite(request);
                    break;
                default:
                    jsonResponse = "{\"success\": false, \"message\": \"Invalid action: " + escapeJsonString(action) + "\"}";
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log error
            jsonResponse = "{\"success\": false, \"message\": \"Invalid number format in request: " + escapeJsonString(e.getMessage()) + "\"}";
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            jsonResponse = "{\"success\": false, \"message\": \"Server error: " + escapeJsonString(e.getMessage()) + "\"}";
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.print(jsonResponse);
        out.flush();
    }

    private CardInput parseCardInputFromRequest(HttpServletRequest request) throws NumberFormatException {
        CardInput cardInput = new CardInput();
        // card_id is not parsed here, as it's specific to update/delete
        cardInput.setName(request.getParameter("name"));
        cardInput.setEmail(request.getParameter("email"));
        cardInput.setRemarks(request.getParameter("remarks"));
        String favoriteParam = request.getParameter("favorite");
        if (favoriteParam != null && !favoriteParam.isEmpty()) {
            cardInput.setFavorite(Boolean.parseBoolean(favoriteParam));
        }

        cardInput.setCompany_name(request.getParameter("company_name"));
        cardInput.setCompany_zipcode(request.getParameter("company_zipcode"));
        cardInput.setCompany_address(request.getParameter("company_address"));
        cardInput.setCompany_phone(request.getParameter("company_phone"));
        cardInput.setDepartment_name(request.getParameter("department_name"));
        cardInput.setPosition_name(request.getParameter("position_name"));
        return cardInput;
    }

    private String handleAddCard(HttpServletRequest request) {
        try {
            CardInput cardInput = parseCardInputFromRequest(request);

            // Handle Company
            Company company = companyDAO.findCompanyByName(cardInput.getCompany_name());
            if (company == null && cardInput.getCompany_name() != null && !cardInput.getCompany_name().isEmpty()) {
                Company newCompany = new Company(0, cardInput.getCompany_name(), cardInput.getCompany_zipcode(), cardInput.getCompany_address(), cardInput.getCompany_phone());
                company = companyDAO.insertCompany(newCompany);
            }
            if (company == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create company.\"}";
            }

            // Handle Department
            Department department = departmentDAO.findDepartmentByName(cardInput.getDepartment_name());
            if (department == null && cardInput.getDepartment_name() != null && !cardInput.getDepartment_name().isEmpty()) {
                Department newDepartment = new Department(0, cardInput.getDepartment_name());
                department = departmentDAO.insertDepartment(newDepartment);
            }
            if (department == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create department.\"}";
            }

            // Handle Position
            Position position = positionDAO.findPositionByName(cardInput.getPosition_name());
            if (position == null && cardInput.getPosition_name() != null && !cardInput.getPosition_name().isEmpty()) {
                Position newPosition = new Position(0, cardInput.getPosition_name());
                position = positionDAO.insertPosition(newPosition);
            }
            if (position == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create position.\"}";
            }

            int newCardId = cardDAO.insertCard(cardInput, company.getCompany_id(), department.getDepartment_id(), position.getPosition_id());
            if (newCardId != -1) {
                return "{\"success\": true, \"card_id\": " + newCardId + "}";
            } else {
                return "{\"success\": false, \"message\": \"Failed to add card.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"success\": false, \"message\": \"Invalid number format for card input: " + escapeJsonString(e.getMessage()) + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error adding card: " + escapeJsonString(e.getMessage()) + "\"}";
        }
    }

    private String handleUpdateCard(HttpServletRequest request) {
        try {
            CardInput cardInput = parseCardInputFromRequest(request);
            String cardIdParam = request.getParameter("card_id");
            if (cardIdParam == null || cardIdParam.isEmpty()) {
                return "{\"success\": false, \"message\": \"Missing card_id for update.\"}";
            }
            cardInput.setCard_id(Integer.parseInt(cardIdParam));


            // Handle Company, Department, Position (similar to add)
            Company company = companyDAO.findCompanyByName(cardInput.getCompany_name());
            if (company == null && cardInput.getCompany_name() != null && !cardInput.getCompany_name().isEmpty()) {
                Company newCompany = new Company(0, cardInput.getCompany_name(), cardInput.getCompany_zipcode(), cardInput.getCompany_address(), cardInput.getCompany_phone());
                company = companyDAO.insertCompany(newCompany);
            }
            if (company == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create company for update.\"}";
            }

            Department department = departmentDAO.findDepartmentByName(cardInput.getDepartment_name());
            if (department == null && cardInput.getDepartment_name() != null && !cardInput.getDepartment_name().isEmpty()) {
                Department newDepartment = new Department(0, cardInput.getDepartment_name());
                department = departmentDAO.insertDepartment(newDepartment);
            }
            if (department == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create department for update.\"}";
            }

            Position position = positionDAO.findPositionByName(cardInput.getPosition_name());
            if (position == null && cardInput.getPosition_name() != null && !cardInput.getPosition_name().isEmpty()) {
                Position newPosition = new Position(0, cardInput.getPosition_name());
                position = positionDAO.insertPosition(newPosition);
            }
            if (position == null) {
                return "{\"success\": false, \"message\": \"Failed to find or create position for update.\"}";
            }

            boolean success = cardDAO.updateCard(cardInput, company.getCompany_id(), department.getDepartment_id(), position.getPosition_id());
            if (success) {
                return "{\"success\": true}";
            } else {
                return "{\"success\": false, \"message\": \"Failed to update card.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"success\": false, \"message\": \"Invalid number format for card_id: " + escapeJsonString(e.getMessage()) + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error updating card: " + escapeJsonString(e.getMessage()) + "\"}";
        }
    }

    private String handleDeleteCard(HttpServletRequest request) {
        try {
            String cardIdParam = request.getParameter("card_id");
            if (cardIdParam == null || cardIdParam.isEmpty()) {
                return "{\"success\": false, \"message\": \"Missing card_id for delete.\"}";
            }
            int cardId = Integer.parseInt(cardIdParam);
            boolean success = cardDAO.deleteCard(cardId);
            if (success) {
                return "{\"success\": true}";
            } else {
                return "{\"success\": false, \"message\": \"Failed to delete card.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"success\": false, \"message\": \"Invalid number format for card_id: " + escapeJsonString(e.getMessage()) + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error deleting card: " + escapeJsonString(e.getMessage()) + "\"}";
        }
    }

    private String handleToggleFavorite(HttpServletRequest request) {
        try {
            String cardIdParam = request.getParameter("card_id");
            String isFavoriteParam = request.getParameter("isFavorite");

            if (cardIdParam == null || cardIdParam.isEmpty() || isFavoriteParam == null || isFavoriteParam.isEmpty()) {
                return "{\"success\": false, \"message\": \"Missing card_id or isFavorite for toggle.\"}";
            }
            int cardId = Integer.parseInt(cardIdParam);
            boolean isFavorite = Boolean.parseBoolean(isFavoriteParam);

            boolean success = cardDAO.updateFavoriteStatus(cardId, isFavorite);
            if (success) {
                return "{\"success\": true}";
            } else {
                return "{\"success\": false, \"message\": \"Failed to update favorite status.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"success\": false, \"message\": \"Invalid number format for card_id: " + escapeJsonString(e.getMessage()) + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error toggling favorite: " + escapeJsonString(e.getMessage()) + "\"}";
        }
    }
}
