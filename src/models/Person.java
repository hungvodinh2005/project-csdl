package models;
public class Person {
    private String idCard;      // CCCD
    private String fullName;    // HoTen
    private java.sql.Date birthDate;  // NgaySinh
    private String gender;      // GioiTinh
    private String phone;       // SDT
    private String address;     // DiaChi
    private String email;       // Email

    // Constructors
    public Person() {}

    public Person(String idCard, String fullName, java.sql.Date birthDate,
                  String gender, String phone, String address, String email) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    // Getters & Setters
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public java.sql.Date getBirthDate() { return birthDate; }
    public void setBirthDate(java.sql.Date birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
