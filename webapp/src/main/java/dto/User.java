package dto;

public class User {
    private int id;
    private String email;
    private String password; // In a real app, this would be hashed

    // Constructors
    public User() {}

    // Updated constructor (salt parameter removed)
    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // getSalt() and setSalt() methods are removed
}
