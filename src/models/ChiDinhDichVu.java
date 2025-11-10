/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;

/**
 *
 * @author CPS
 */
public class ChiDinhDichVu {
    private String mahs, madv, ketqua;
    private LocalDate ngay;

    public ChiDinhDichVu(String mahs, String madv, LocalDate ngay) {
        this.mahs = mahs;
        this.madv = madv;
        this.ngay = ngay;
    }

    public ChiDinhDichVu(String mahs, String madv) {
        this.mahs = mahs;
        this.madv = madv;
    }

    public String getMahs() {
        return mahs;
    }

    public void setMahs(String mahs) {
        this.mahs = mahs;
    }

    public String getMadv() {
        return madv;
    }

    public void setMadv(String madv) {
        this.madv = madv;
    }

    public String getKetqua() {
        return ketqua;
    }

    public void setKetqua(String ketqua) {
        this.ketqua = ketqua;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }
    
}
