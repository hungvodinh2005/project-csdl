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
public class HoaDon {
    private String mahd, mahs, hinhthuc;
    private int tiendichvu, tienthuoc, tongtien;
    private LocalDate ngaythanhtoan;

    public HoaDon(String mahd, String mahs, String hinhthuc, int tiendichvu, int tienthuoc, int tongtien, LocalDate ngaythanhtoan) {
        this.mahd = mahd;
        this.mahs = mahs;
        this.hinhthuc = hinhthuc;
        this.tiendichvu = tiendichvu;
        this.tienthuoc = tienthuoc;
        this.tongtien = tongtien;
        this.ngaythanhtoan = ngaythanhtoan;
    }

    public String getMahd() {
        return mahd;
    }

    public String getMahs() {
        return mahs;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public int getTiendichvu() {
        return tiendichvu;
    }

    public int getTienthuoc() {
        return tienthuoc;
    }

    public int getTongtien() {
        return tongtien;
    }

    public LocalDate getNgaythanhtoan() {
        return ngaythanhtoan;
    }
    
}
