package dto;

import java.io.Serializable;

public class Company implements Serializable {
    private int company_id;
    private String company_name;
    private String zipcode;
    private String address;
    private String phone;

    public Company() {}

    public Company(int company_id, String company_name, String zipcode, String address, String phone) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.zipcode = zipcode;
        this.address = address;
        this.phone = phone;
    }

    // Getters and Setters
    public int getCompany_id() { return company_id; }
    public void setCompany_id(int company_id) { this.company_id = company_id; }
    public String getCompany_name() { return company_name; }
    public void setCompany_name(String company_name) { this.company_name = company_name; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
