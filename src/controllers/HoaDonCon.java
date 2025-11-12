/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import connection.JDBCUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.HoaDon;
/**
 *
 * @author CPS
 */
public class HoaDonCon {
    public String getMaHoaDon(){
        String maHD="";
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from tinhtien order by mahoadon desc limit 1";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            String ma = rs.getString("mahoadon");
            int so = Integer.parseInt(ma.substring(2));
            so++;
            maHD=String.format("HD%03d", so);
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maHD;
    }
    
    public int insert(HoaDon h){
        Connection con = JDBCUtil.getConnection();
        String sql = "insert into tinhtien(mahoso, mahoadon, ngaythanhtoan, tienthuoc, tiendicvu, tongtien, hinhthucthanhtoan)"
                + "values (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, h.getMahs());
            pst.setString(2, h.getMahd());
            pst.setString(7, h.getHinhthuc());
            pst.setInt(4, h.getTienthuoc());
            pst.setInt(5, h.getTiendichvu());
            pst.setInt(6, h.getTongtien());
            pst.setDate(3, Date.valueOf(h.getNgaythanhtoan()));
            int kq = pst.executeUpdate();
            System.out.println("thanh toan thanh cong");
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
        public int updateNgayXuatVien(LocalDate today, String mahs){
        Connection con = JDBCUtil.getConnection();
        String sql="update hosobenhan set ngayxuatvien = ? where mahoso = ? and ngayxuatvien is null";
        PreparedStatement pst;
        try {
            pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(today));
            pst.setString(2, mahs);
            int kq = pst.executeUpdate();
            System.out.println("update thanh cong");
        } catch (SQLException ex) {
            Logger.getLogger(MedicalRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
