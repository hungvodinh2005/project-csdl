/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import models.ChiDinhDichVu;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class ChiDinhCon {
    public int insertChiDinh(ChiDinhDichVu cd){
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        String sql="insert into chidinhdichvu(mahoso, madichvu)"
                + "values(?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, cd.getMahs());
            pst.setString(2, cd.getMadv());
            kq= pst.executeUpdate();
            System.out.println("insert thanh cong");
            pst.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChiDinhCon.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kq;
    }
}
