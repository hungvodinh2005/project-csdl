/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import connection.JDBCUtil;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.connectMysql; 
import models.Patient;         
import java.util.*;
import models.Guardian;


public class PatientController {
    public String generatePatientId() {
    String newId = "BN001";
    String sql = "SELECT MaBenhNhan FROM BenhNhan ORDER BY MaBenhNhan DESC LIMIT 1";
    try {
        Connection conn = new connectMysql().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String lastId = rs.getString("MaBenhNhan"); // ví dụ BN023
            int number = Integer.parseInt(lastId.substring(2)) + 1;
            newId = String.format("BN%03d", number);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return newId;
}

   public List<Patient> getAllPatients() {
    ArrayList<Patient> list = new ArrayList<>();

    String sql = "SELECT mabenhnhan, cccd, manguoigiamho, mabhyt FROM benhnhan";

    try (Connection conn = new connectMysql().getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Patient p = new Patient();
            p.setPatientId(rs.getString("mabenhnhan"));
            p.setIdCard(rs.getString("cccd"));
            p.setGuardianId(rs.getString("manguoigiamho"));
            p.setMaBhyt(rs.getString("mabhyt"));
            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
   public boolean addPatient(Patient p) {
    String sql = """
        INSERT INTO benhnhan (mabenhnhan, cccd, manguoigiamho, mabhyt)
        VALUES (?, ?, ?, ?)
    """;

    try (Connection conn = new connectMysql().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, p.getPatientId());
        ps.setString(2, p.getIdCard());
        ps.setString(3, p.getGuardianId());
        ps.setString(4, p.getMaBhyt());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public boolean updatePatient(Patient p) {
    String sql = """
        UPDATE benhnhan
        SET manguoigiamho = ?, mabhyt = ?
        WHERE mabenhnhan = ?
    """;

    try (Connection conn = new connectMysql().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, p.getGuardianId());
        ps.setString(2, p.getMaBhyt());
        ps.setString(3, p.getPatientId());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public boolean deletePatient(String patientId) {
    String sql = "DELETE FROM benhnhan WHERE mabenhnhan = ?";

    try (Connection conn = new connectMysql().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, patientId);
        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

   
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
    public String getGuardianName(String maGiamHo) {
        String hoTen = null;

        String sql = "SELECT n.hoten " +
                     "FROM nguoi n " +
                     "JOIN nguoigiamho g ON n.cccd = g.cccd " +
                     "WHERE g.manguoigiamho = ?";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGiamHo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hoTen = rs.getString("hoten");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hoTen;
    }
    public List<Guardian> getAllGuardians() {
        List<Guardian> list = new ArrayList<>();
        String sql = "SELECT g.manguoigiamho, n.hoten " +
                     "FROM nguoigiamho g " +
                     "JOIN nguoi n ON g.cccd = n.cccd " +
                     "ORDER BY n.hoten ASC";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("manguoigiamho");
                String name = rs.getString("hoten");
                list.add(new Guardian(id, name));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}



//         PatientController pc = new PatientController();
//         System.out.println(pc.searchPatient("BN001"));
//     }
// }
