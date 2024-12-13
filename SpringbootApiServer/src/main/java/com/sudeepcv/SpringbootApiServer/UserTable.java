package com.sudeepcv.SpringbootApiServer;



        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;

@Entity
public class UserTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String mobileNumber;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Constructors
    public UserTable() {
    }

    public UserTable(String username, String mobileNumber) {
        this.username = username;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
