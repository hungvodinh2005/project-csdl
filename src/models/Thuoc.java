package models;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private String donViTinh;
    private String moTa;
    private double gia;
    private String nhasanxuat; // <-- ĐÃ THÊM

    // Constructor, Getters, Setters
    public Thuoc() {}

    public String getMaThuoc() { return maThuoc; }
    public void setMaThuoc(String maThuoc) { this.maThuoc = maThuoc; }
    public String getTenThuoc() { return tenThuoc; }
    public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }
    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    
    // Getter/Setter cho trường mới
    public String getNhasanxuat() { return nhasanxuat; }
    public void setNhasanxuat(String nhasanxuat) { this.nhasanxuat = nhasanxuat; }

    @Override
    public String toString() {
        // JComboBox sẽ hiển thị Tên Thuốc (ví dụ: "Amoxicillin 500mg")
        return this.tenThuoc; 
    }
}