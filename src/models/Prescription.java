package models;

import java.util.Date; // Dùng java.util.Date để lưu ngày

/**
 * Đây là lớp Model "ảo" (View Model), dùng để chứa dữ liệu 
 * được kết hợp từ nhiều bảng, cụ thể là LichSuKham và DonThuoc.
 */
public class Prescription {

    private String maDon;
    private String maBenhNhan;
    private String maBacSi;
    private Date ngayKeDon; // Sẽ lấy từ NgayGioKham của bảng LichSuKham
    private String lieuDung;

    // --- Constructors ---
    
    // Constructor rỗng
    public Prescription() {
    }

    // Constructor đầy đủ
    public Prescription(String maDon, String maBenhNhan, String maBacSi, Date ngayKeDon, String lieuDung) {
        this.maDon = maDon;
        this.maBenhNhan = maBenhNhan;
        this.maBacSi = maBacSi;
        this.ngayKeDon = ngayKeDon;
        this.lieuDung = lieuDung;
    }

    // --- Getters and Setters ---
    // (Bắt buộc phải có để truy cập và thay đổi dữ liệu)
    
    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public String getMaBenhNhan() {
        return maBenhNhan;
    }

    public void setMaBenhNhan(String maBenhNhan) {
        this.maBenhNhan = maBenhNhan;
    }

    public String getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(String maBacSi) {
        this.maBacSi = maBacSi;
    }

    public Date getNgayKeDon() {
        return ngayKeDon;
    }

    public void setNgayKeDon(Date ngayKeDon) {
        this.ngayKeDon = ngayKeDon;
    }

    public String getLieuDung() {
        return lieuDung;
    }

    public void setLieuDung(String lieuDung) {
        this.lieuDung = lieuDung;
    }
}