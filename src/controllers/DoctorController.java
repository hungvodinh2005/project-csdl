/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import connection.connectMysql;
import models.Doctor;

/**
 *
 * @author DINHHUNG
 */
public class DoctorController {

    public String generateDoctorId() {
        String newId = "BS001";
        String sql = "SELECT MaBacSi FROM BacSi ORDER BY MaBacSi DESC LIMIT 1";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString("MaBacSi"); 
                int number = Integer.parseInt(lastId.substring(2)) + 1;
                newId = String.format("BS%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    public void createDoctor(Doctor doc) {
        String sql = "INSERT INTO BacSi (MaBacSi, CCCD, ChuyenKhoa) VALUES (?, ?, ?)";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement create = conn.prepareStatement(sql);
            create.setString(1, doc.getDoctorId());
            create.setString(2, doc.getIdCard());
            create.setString(3, doc.getSpecialty());
            create.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public ArrayList<Doctor> showAllDoctors() {
        ArrayList<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM BacSi where TrangThai='Đang làm'";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement show = conn.prepareStatement(sql);
            ResultSet rs = show.executeQuery();
            while (rs.next()) {
                Doctor doc = new Doctor();
                doc.setDoctorId(rs.getString("MaBacSi"));
                doc.setIdCard(rs.getString("CCCD"));
                doc.setSpecialty(rs.getString("ChuyenKhoa"));
                list.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   
    public void editDoctor(Doctor doc) {
        String sql = "UPDATE BacSi SET CCCD = ?, ChuyenKhoa = ? WHERE MaBacSi = ?";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement edit = conn.prepareStatement(sql);
            edit.setString(1, doc.getIdCard());
            edit.setString(2, doc.getSpecialty());
            edit.setString(3, doc.getDoctorId());
            edit.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
   public void setDoctorInactive(String doctorId) {
    String sql = "UPDATE BacSi SET TrangThai = 'Nghỉ làm' WHERE MaBacSi = ?";
    try (Connection conn = new connectMysql().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, doctorId);
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   
    public ArrayList<Doctor> searchBySpecialty(String specialty) {
        ArrayList<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM BacSi WHERE ChuyenKhoa LIKE ?";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement search = conn.prepareStatement(sql);
            search.setString(1, "%" + specialty + "%");
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                Doctor doc = new Doctor();
                doc.setDoctorId(rs.getString("MaBacSi"));
                doc.setIdCard(rs.getString("CCCD"));
                doc.setSpecialty(rs.getString("ChuyenKhoa"));
                list.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getDoctorNames(String CCCD) {
    String sql = "SELECT HoTen FROM Nguoi where CCCD=?" ;
    String result="";
    try {
        Connection conn = new connectMysql().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, CCCD);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {  
            result = rs.getString("HoTen");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
public boolean isCardIdExists(String cardId) {
    boolean exists = false;
    String sql = "SELECT COUNT(*) FROM doctor WHERE idCard = ?";
    try {
        Connection conn = new connectMysql().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cardId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            exists = rs.getInt(1) > 0; 
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return exists;
}
}
