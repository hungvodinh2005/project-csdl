package models;

public class Service {
    private String maDichVu;
    private String tenDichVu;
    private double chiPhi; // Sử dụng double cho Chi Phí

    // Constructors
    public Service() {}

    public Service(String maDichVu, String tenDichVu, double chiPhi) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.chiPhi = chiPhi;
    }

    // Getters and Setters
    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public double getChiPhi() {
        return chiPhi;
    }

    public void setChiPhi(double chiPhi) {
        this.chiPhi = chiPhi;
    }
}