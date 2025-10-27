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
//  * @author CPS
//  */
// public class PatientController {
//     public String searchPatient(String maBN){
//         Connection con = JDBCUtil.getConnection();
//         Patient bn = new Patient();
//         String sql = "select * from Nguoi n where n.cccd in (select b.cccd from benhnhan b where b.MaBenhNhan = ?) ";
//         String tenBN="";
//         try {
//             PreparedStatement pst = con.prepareStatement(sql);
//             pst.setString(1, maBN);
//             ResultSet rs = pst.executeQuery();
//             rs.next();
//             tenBN = rs.getString("hoten");
//             System.out.println(tenBN);
//             rs.close();
//             pst.close();
//             con.close();
//         } catch (SQLException ex) {
//             Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
//         }
//         return tenBN;
//     }
//     public static void main(String[] args) {
package controllers;

import connection.connectMysql; 
import models.Patient;         

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller này xử lý logic cho Trang 2 (Quản lý Bệnh nhân)
 * Nó dùng model 'Patient' của nhóm và kết nối với bảng 'nguoi', 'benhnhan'
 */
public class PatientController {

    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT b.mabenhnhan, b.cccd, n.hoten, b.ngh_cccd, b.tiensubenhan " +
                     "FROM benhnhan b JOIN nguoi n ON b.cccd = n.cccd"; 

        try (Connection conn = new connectMysql().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(); 
                p.setPatientId(rs.getString("mabenhnhan"));
                p.setIdCard(rs.getString("cccd"));
                p.setHoTen(rs.getString("hoten")); 
                p.setGuardianId(rs.getString("ngh_cccd"));
                p.setMedicalHistory(rs.getString("tiensubenhan"));
                patientList.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return patientList;
    }

    public boolean addPatient(Patient p) {
        Connection conn = null;
        PreparedStatement pstmtNguoi = null, pstmtBenhNhan = null;
        String sqlNguoi = "INSERT INTO nguoi (cccd, hoten) VALUES (?, ?)";
        String sqlBenhNhan = "INSERT INTO benhnhan (mabenhnhan, cccd, ngh_cccd, tiensubenhan) VALUES (?, ?, ?, ?)";
        
        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false); 
            
            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, p.getIdCard()); 
            pstmtNguoi.setString(2, p.getHoTen());  
            pstmtNguoi.executeUpdate();

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, p.getPatientId()); 
            pstmtBenhNhan.setString(2, p.getIdCard());
            pstmtBenhNhan.setString(3, p.getGuardianId());
            pstmtBenhNhan.setString(4, p.getMedicalHistory());
            pstmtBenhNhan.executeUpdate();

            conn.commit(); 
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean updatePatient(Patient p) {
        Connection conn = null;
        PreparedStatement pstmtNguoi = null, pstmtBenhNhan = null;
        String sqlNguoi = "UPDATE nguoi SET hoten = ? WHERE cccd = ?";
        String sqlBenhNhan = "UPDATE benhnhan SET ngh_cccd = ?, tiensubenhan = ? WHERE mabenhnhan = ?";

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false); 
            
            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, p.getHoTen());
            pstmtNguoi.setString(2, p.getIdCard()); 
            pstmtNguoi.executeUpdate();

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, p.getGuardianId());
            pstmtBenhNhan.setString(2, p.getMedicalHistory());
            pstmtBenhNhan.setString(3, p.getPatientId()); 
            pstmtBenhNhan.executeUpdate();

            conn.commit(); 
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
             try {
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean deletePatient(String patientId, String cccd) {
        Connection conn = null;
        PreparedStatement pstmtBenhNhan = null, pstmtNguoi = null;
        String sqlBenhNhan = "DELETE FROM benhnhan WHERE mabenhnhan = ?";
        String sqlNguoi = "DELETE FROM nguoi WHERE cccd = ?"; 

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false); 
            
            // LƯU Ý: Cần xóa các bảng con (như LichSuKham) trước
            
            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, patientId);
            pstmtBenhNhan.executeUpdate();

            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, cccd);
            pstmtNguoi.executeUpdate();
            
            conn.commit(); 
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
             try {
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Patient> searchPatients(String keyword) {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT b.mabenhnhan, b.cccd, n.hoten, b.ngh_cccd, b.tiensubenhan " +
                     "FROM benhnhan b JOIN nguoi n ON b.cccd = n.cccd " +
                     "WHERE n.hoten LIKE ? OR b.cccd LIKE ? OR b.mabenhnhan LIKE ?";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + keyword + "%";
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient();
                    p.setPatientId(rs.getString("mabenhnhan"));
                    p.setIdCard(rs.getString("cccd"));
                    p.setHoTen(rs.getString("hoten")); 
                    p.setGuardianId(rs.getString("ngh_cccd"));
                    p.setMedicalHistory(rs.getString("tiensubenhan"));
                    patientList.add(p);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return patientList;
    }
}



//         PatientController pc = new PatientController();
//         System.out.println(pc.searchPatient("BN001"));
//     }
// }
