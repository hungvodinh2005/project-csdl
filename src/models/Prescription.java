package models;

import java.util.Date;

public class Prescription {

    private String maDon;       // Mã đơn thuốc (tự tăng)
    private String maHoSo;   // Khóa ngoại liên kết với HoSoBenhAn
    private Date ngayKeDon;  // Ngày kê đơn
    private String maBacSi;  // Khóa ngoại liên kết với BacSi

    public Prescription() {
    }

    public Prescription(String maHoSo, Date ngayKeDon, String maBacSi) {
        this.maHoSo = maHoSo;
        this.ngayKeDon = ngayKeDon;
        this.maBacSi = maBacSi;
    }

    public String getMaDon() {
        return maDon;
    }

    public void setMaDon(String maDon) {
        this.maDon = maDon;
    }

    public String getMaHoSo() {
        return maHoSo;
    }

    public void setMaHoSo(String maHoSo) {
        this.maHoSo = maHoSo;
    }

    public Date getNgayKeDon() {
        return ngayKeDon;
    }

    public void setNgayKeDon(Date ngayKeDon) {
        this.ngayKeDon = ngayKeDon;
    }

    public String getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(String maBacSi) {
        this.maBacSi = maBacSi;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "maDon=" + maDon +
                ", maHoSo='" + maHoSo + '\'' +
                ", ngayKeDon=" + ngayKeDon +
                ", maBacSi='" + maBacSi + '\'' +
                '}';
    }
}