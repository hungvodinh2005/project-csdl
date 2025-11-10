package controllers;

import connection.JDBCUtil; 
import models.Prescription;
import models.Thuoc;
import models.ChiTietDonThuoc;


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
    
//Lấy tất cả thuốc từ CSDL (cho comboMaThuoc)
    public List<Thuoc> getAllThuoc() {
        List<Thuoc> dsThuoc = new ArrayList<>();
        Connection con = JDBCUtil.getConnection(); // Dùng file kết nối của bạn
        
        // SỬA LẠI: Thêm "nhasanxuat" vào câu SELECT
        String sql = "SELECT maThuoc, tenThuoc, donViTinh, moTa, gia, nhasanxuat FROM thuoc"; 

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setMaThuoc(rs.getString("maThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setDonViTinh(rs.getString("donViTinh"));
                t.setMoTa(rs.getString("moTa")); // Lấy luôn mô tả
                t.setGia(rs.getDouble("gia"));
                t.setNhasanxuat(rs.getString("nhasanxuat")); // <-- ĐÃ THÊM
                
                dsThuoc.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code đóng kết nối pst, rs, con) ...
        }
        return dsThuoc;
    }

//Lấy tất cả MaDon đã có (cho comboMaDon)
    public List<String> getAllPrescriptionIDs() {
        List<String> idList = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT MaDon FROM donthuoc ORDER BY MaDon"; 
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                idList.add(rs.getString("MaDon"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code đóng kết nối pst, rs, con) ...
        }
        return idList;
    }

//Lấy chi tiết của MỘT đơn thuốc (cho JTable)
    public List<ChiTietDonThuoc> getChiTietDonThuoc(String maDon) {
        List<ChiTietDonThuoc> dsChiTiet = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        
        String sql = "SELECT ct.maDon, ct.soLuong, ct.lieuDung, ct.gia, " +
                     "t.maThuoc, t.tenThuoc, t.donViTinh " +
                     "FROM chitietdonthuoc ct " +
                     "JOIN thuoc t ON ct.maThuoc = t.maThuoc " +
                     "WHERE ct.maDon = ?";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, maDon);
            rs = pst.executeQuery();

            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setMaThuoc(rs.getString("maThuoc"));
                t.setTenThuoc(rs.getString("tenThuoc"));
                t.setDonViTinh(rs.getString("donViTinh"));
                
                ChiTietDonThuoc ct = new ChiTietDonThuoc();
                ct.setMaDon(rs.getString("maDon"));
                ct.setThuoc(t);
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setLieuDung(rs.getString("lieuDung"));
                ct.setGia(rs.getDouble("gia"));
                
                dsChiTiet.add(ct);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // ... (code đóng kết nối pst, rs, con) ...
        }
        return dsChiTiet;
    }

//    Thêm một chi tiết đơn thuốc (cho nút "Thêm" ở Tab 2)
    public int insertChiTiet(ChiTietDonThuoc chiTiet) {
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO chitietdonthuoc (maDon, maThuoc, soLuong, lieuDung, gia) VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, chiTiet.getMaDon());
            pst.setString(2, chiTiet.getThuoc().getMaThuoc());
            pst.setInt(3, chiTiet.getSoLuong());
            pst.setString(4, chiTiet.getLieuDung());
            pst.setDouble(5, chiTiet.getGia());
            
            kq = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // ... (code đóng kết nối pst, con) ...
        }
        return kq;
    }

//Xóa một chi tiết đơn thuốc (cho nút "Xóa" ở Tab 2)
    public int deleteChiTiet(String maDon, String maThuoc) {
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "DELETE FROM chitietdonthuoc WHERE maDon = ? AND maThuoc = ?";
        
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, maDon);
            pst.setString(2, maThuoc);
            kq = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // ... (code đóng kết nối pst, con) ...
        }
        return kq;
    }
}