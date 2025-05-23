package dto;

import java.io.Serializable;

public class CardInput implements Serializable {
    private int card_id; // For updates, 0 or not set for inserts
    private String name;
    private String email;
    private String remarks;
    private boolean favorite;

    private String company_name;
    private String company_zipcode;
    private String company_address;
    private String company_phone;
    private String department_name;
    private String position_name;

    public CardInput() {}

    // All-args constructor (optional, getters/setters are more important)
    public CardInput(int card_id, String name, String email, String remarks, boolean favorite,
                     String company_name, String company_zipcode, String company_address, String company_phone,
                     String department_name, String position_name) {
        this.card_id = card_id;
        this.name = name;
        this.email = email;
        this.remarks = remarks;
        this.favorite = favorite;
        this.company_name = company_name;
        this.company_zipcode = company_zipcode;
        this.company_address = company_address;
        this.company_phone = company_phone;
        this.department_name = department_name;
        this.position_name = position_name;
    }

    // Getters and Setters for all fields
    public int getCard_id() { return card_id; }
    public void setCard_id(int card_id) { this.card_id = card_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public String getCompany_name() { return company_name; }
    public void setCompany_name(String company_name) { this.company_name = company_name; }
    public String getCompany_zipcode() { return company_zipcode; }
    public void setCompany_zipcode(String company_zipcode) { this.company_zipcode = company_zipcode; }
    public String getCompany_address() { return company_address; }
    public void setCompany_address(String company_address) { this.company_address = company_address; }
    public String getCompany_phone() { return company_phone; }
    public void setCompany_phone(String company_phone) { this.company_phone = company_phone; }
    public String getDepartment_name() { return department_name; }
    public void setDepartment_name(String department_name) { this.department_name = department_name; }
    public String getPosition_name() { return position_name; }
    public void setPosition_name(String position_name) { this.position_name = position_name; }
}
