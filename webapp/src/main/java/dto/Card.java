package dto;

public class Card {
    private int cardId;
    private String companyName;
    private String companyZipcode;
    private String companyAddress;
    private String companyPhone;
    private String name;
    private String email;
    private String remarks;
    private boolean favorite;
    private String departmentName;
    private String positionName;
    private String createdAt;

    // Getter & Setter（略記）

    public int getCardId() {
        return cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyZipcode() {
        return companyZipcode;
    }
    public void setCompanyZipcode(String companyZipcode) {
        this.companyZipcode = companyZipcode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }
    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
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

    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
