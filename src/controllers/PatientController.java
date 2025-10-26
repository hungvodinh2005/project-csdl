/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import connection.JDBCUtil;
import java.sql.*;
import connection.connectMysql;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Patient;
/**
 *
 * @author CPS
 */
public class PatientController {
    public String searchPatient(String maBN){
        Connection con = JDBCUtil.getConnection();
        Patient bn = new Patient();
        String sql = "select * from Nguoi n where n.cccd in (select b.cccd from benhnhan b where b.MaBenhNhan = ?) ";
        String tenBN="";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maBN);
            ResultSet rs = pst.executeQuery();
            rs.next();
            tenBN = rs.getString("hoten");
            System.out.println(tenBN);
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tenBN;
    }
    public static void main(String[] args) {
        PatientController pc = new PatientController();
        System.out.println(pc.searchPatient("BN001"));
    }
}
