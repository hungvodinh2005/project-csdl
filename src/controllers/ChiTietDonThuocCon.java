/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class ChiTietDonThuocCon {
    public static int getGiatheoMaDon(String maDon){
        int tonggia=0;
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from chitietdonthuoc where madon = ?";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maDon);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                int soLuong = rs.getInt("soluong");
                int gia = rs.getInt("gia");
                tonggia+=(soLuong*gia);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietDonThuocCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tonggia;
    }
    
    public HashMap<String, Integer> getMaAndPrice(String maHS){
        HashMap<String, Integer> map = new HashMap<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from donthuoc where mahoso = ?";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maHS);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String maDon = rs.getString("madon");
                int gia = getGiatheoMaDon(maDon);
                map.put(maDon, gia);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietDonThuocCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return map;
    }
}
