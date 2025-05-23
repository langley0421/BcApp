package dto;

import java.io.Serializable;
import java.sql.Timestamp; // Or java.util.Date

public class CardInfo implements Serializable {
    private int card_id;
    private String name;
    private String email;
    private String remarks;
    private boolean favorite;
    private Timestamp created_date; // Or java.util.Date
    private Timestamp update_date;  // Or java.util.Date

    // Fields from joined tables
    private String company_name;
    private String company_zipcode;
    private String company_address;
    private String company_phone;
    private String department_name;
    private String position_name;

    // Foreign key IDs (optional, but can be useful)
    private int company_id;
    private int department_id;
    private int position_id;

    // Default constructor
    public CardInfo() {
    }

    // All-args constructor (or use a builder pattern if it gets too long)
    public CardInfo(int card_id, String name, String email, String remarks, boolean favorite, Timestamp created_date, Timestamp update_date,
                    String company_name, String company_zipcode, String company_address, String company_phone,
                    String department_name, String position_name, int company_id, int department_id, int position_id) {
        this.card_id = card_id;
        this.name = name;
        this.email = email;
        this.remarks = remarks;
        this.favorite = favorite;
        this.created_date = created_date;
        this.update_date = update_date;
        this.company_name = company_name;
        this.company_zipcode = company_zipcode;
        this.company_address = company_address;
        this.company_phone = company_phone;
        this.department_name = department_name;
        this.position_name = position_name;
        this.company_id = company_id;
        this.department_id = department_id;
        this.position_id = position_id;
    }

    // Getters and Setters for all fields

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public Timestamp getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Timestamp update_date) {
        this.update_date = update_date;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_zipcode() {
        return company_zipcode;
    }

    public void setCompany_zipcode(String company_zipcode) {
        this.company_zipcode = company_zipcode;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }
}
