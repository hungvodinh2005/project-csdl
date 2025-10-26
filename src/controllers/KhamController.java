/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.LicSuKham;

/**
 *
 * @author CPS
 */
public class KhamController {
    public int insert(String maBN,String maBS){
        int kq=0;
        Connection con = JDBCUtil.getConnection();
        String sql="insert into kham (mabenhnhan, mabacsi) values(?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maBN);
            pst.setString(2, maBS);
            kq=pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LichSuKhamController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kq;
    }
}
