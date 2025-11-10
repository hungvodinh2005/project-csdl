package models;

public class ChiTietDonThuoc {
    private String maDon;
    private Thuoc thuoc; // Dùng đối tượng Thuoc
    private int soLuong;
    private String lieuDung;
    private double gia; // 'Gia' trong bảng ChiTietDonThuoc (Giá tổng = SL * Đơn giá)

    // Constructor, Getters, Setters
    public ChiTietDonThuoc() {}

    public String getMaDon() { return maDon; }
    public void setMaDon(String maDon) { this.maDon = maDon; }
    public Thuoc getThuoc() { return thuoc; }
    public void setThuoc(Thuoc thuoc) { this.thuoc = thuoc; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public String getLieuDung() { return lieuDung; }
    public void setLieuDung(String lieuDung) { this.lieuDung = lieuDung; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
}