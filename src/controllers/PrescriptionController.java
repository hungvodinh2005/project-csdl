package controllers;

import connection.JDBCUtil; 
import models.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PrescriptionController {

    // Thêm một đơn thuốc mới vào CSDL.
    public int insert(Prescription prescription) {
        int kq = 0;
        Connection con = JDBCUtil.getConnection(); 
        
        // SỬA LẠI: Bỏ MaBacSi. Tên bảng và cột dùng chữ thường
        String sql = "INSERT INTO donthuoc (MaDon, MaHoSo, ngaykedon) VALUES (?, ?, ?)";

        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            
            // SỬA LẠI: Chỉ set 3 tham số
            pst.setString(1, prescription.getMaDon());
            pst.setString(2, prescription.getMaHoSo());
            pst.setTimestamp(3, new Timestamp(prescription.getNgayKeDon().getTime()));
            // Đã xóa dòng pst.setString(4, ...)

            kq = pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return kq;
    }

    // Tạo MaDon tiếp theo dựa trên MaDon cuối cùng trong CSDL.
    public String nextPrescriptionID() {
        String nextID = "";
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Tên bảng chữ thường
        String sql = "SELECT MaDon FROM donthuoc ORDER BY MaDon DESC LIMIT 1";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("MaDon"); 
                int ma = Integer.parseInt(lastID.substring(2)); 
                nextID = String.format("D%02d", ma + 1);
            } else {
                nextID = "D01";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, "Lỗi định dạng MaDon", e);
            throw new RuntimeException("Lỗi định dạng ID trong CSDL", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return nextID;
    }
    
    // Lấy tất cả đơn thuốc (SỬA LẠI HOÀN TOÀN)
    public java.util.List<Prescription> getAllPrescriptions() {
        java.util.List<Prescription> prescriptionList = new java.util.ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Dùng JOIN và tên chữ thường
        String sql = "SELECT dt.MaDon, dt.MaHoSo, dt.ngaykedon, hs.mabacsi " +
                     "FROM donthuoc dt " +
                     "JOIN hosobenhan hs ON dt.MaHoSo = hs.MaHoSo " +
                     "ORDER BY dt.ngaykedon DESC";

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setMaDon(rs.getString("MaDon"));
                p.setMaHoSo(rs.getString("MaHoSo"));
                p.setNgayKeDon(rs.getTimestamp("ngaykedon")); // Sửa tên cột
                p.setMaBacSi(rs.getString("mabacsi")); // Sửa tên cột
                
                prescriptionList.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return prescriptionList;
    }

    // Xóa một đơn thuốc
    public int delete(String maDon) {
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Tên bảng chữ thường
        String sql = "DELETE FROM donthuoc WHERE MaDon = ?";

        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, maDon);
            kq = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return kq;
    }
    
    // Lấy Mã Hồ Sơ
    public List<String> getAllMedicalRecordIDs() {
        List<String> recordIDs = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Tên bảng chữ thường
        String sql = "SELECT DISTINCT MaHoSo FROM hosobenhan ORDER BY MaHoSo"; 
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                recordIDs.add(rs.getString("MaHoSo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code finally giữ nguyên) ...
        }
        return recordIDs;
    }
    
    // Lấy Mã Bác Sĩ
    public List<String> getAllDoctorIDs() {
        List<String> doctorIDs = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Tên bảng và cột chữ thường
        String sql = "SELECT mabacsi FROM bacsi ORDER BY mabacsi"; 
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                doctorIDs.add(rs.getString("mabacsi")); // Sửa tên cột
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code finally giữ nguyên) ...
        }
        return doctorIDs;
    }
    
    // Lấy MaBacSi từ MaHoSo
    public String getMaBacSiFromHoSo(String maHoSo) {
        String maBacSi = null;
        Connection con = JDBCUtil.getConnection();
        
        // SỬA LẠI: Tên bảng và cột chữ thường
        String sql = "SELECT mabacsi FROM hosobenhan WHERE MaHoSo = ?"; 
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, maHoSo);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                maBacSi = rs.getString("mabacsi"); // Sửa tên cột
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code finally giữ nguyên) ...
        }
        return maBacSi;
    }
}