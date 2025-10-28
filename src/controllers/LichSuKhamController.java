/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import models.LicSuKham;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class LichSuKhamController {
    public int insert(LicSuKham ls){
        int kq=0;
        Connection con = JDBCUtil.getConnection();
        String sql="insert into lichsukham (mahoSo, mabacsi, mabenhnhan, ngaygiokham) values(?, ?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, ls.getRecordID());
            pst.setString(2, ls.getDoctorID());
            pst.setString(3, ls.getPatientID());
            pst.setTimestamp(4, Timestamp.valueOf(ls.getTime()));
            kq=pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LichSuKhamController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kq;
    }
    public String nextRecordID(){
        String recordID="";
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from lichsukham order by mahoso desc limit 1";
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            recordID = rs.getString("madon");
            int ma = Integer.parseInt(recordID.substring(2));
            recordID = String.format("DT%03d", ma+1);
            con.close();
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recordID;
        
    }
}
