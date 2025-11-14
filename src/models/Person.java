package models;

public class Person {

    private String cccd;           // cccd
    private String hoten;          // hoten
    private java.sql.Date ngaysinh; // ngaysinh
    private String mabhyt;         // mabhyt
    private String sdt;            // sdt
    private String email;          // email
    private String diachi;         // diachi

    // Constructors
    public Person() {}

    public Person(String cccd, String hoten, java.sql.Date ngaysinh,
                  String mabhyt, String sdt, String email, String diachi) {
        this.cccd = cccd;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.mabhyt = mabhyt;
        this.sdt = sdt;
        this.email = email;
        this.diachi = diachi;
    }

    // Getters & Setters
    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public java.sql.Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(java.sql.Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getMabhyt() {
        return mabhyt;
    }

    public void setMabhyt(String mabhyt) {
        this.mabhyt = mabhyt;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
