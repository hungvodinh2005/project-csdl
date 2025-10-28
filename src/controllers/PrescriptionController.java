package controllers;

import java.sql.*;
import java.util.*;
import connection.connectMysql;
import models.Prescription;
import java.util.Date;

public class PrescriptionController {
    
    //tạo đơn thuốc mới và lịch sử khám mới
    public void createPrescription(Prescription p) {
        
        // 2 câu lệnh SQL cho 2 bảng
        String sqlDonThuoc = "INSERT INTO DonThuoc (MaDon, MaBacSi, LieuDung, NgayKeDon) VALUES (?, ?, ?, ?)";
        String sqlLichSuKham = "INSERT INTO LichSuKham (MaDon, MaBenhNhan, MaBacSi, NgayGioKham) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmtDonThuoc = null;
        PreparedStatement stmtLichSuKham = null;

        try {
            // 1. Lấy kết nối
            conn = new connectMysql().getConnection();
            
            // --- BẮT ĐẦU TRANSACTION ---
            conn.setAutoCommit(false); 
            
            // --- 2. Thêm vào bảng DonThuoc ---
            stmtDonThuoc = conn.prepareStatement(sqlDonThuoc);
            stmtDonThuoc.setString(1, p.getMaDon());
            stmtDonThuoc.setString(2, p.getMaBacSi());
            stmtDonThuoc.setString(3, p.getLieuDung());
            // Chuyển java.util.Date sang java.sql.Timestamp
            stmtDonThuoc.setTimestamp(4, new java.sql.Timestamp(p.getNgayKeDon().getTime())); 
            stmtDonThuoc.executeUpdate();

            // --- 3. Thêm vào bảng LichSuKham ---
            stmtLichSuKham = conn.prepareStatement(sqlLichSuKham);
            stmtLichSuKham.setString(1, p.getMaDon());
            stmtLichSuKham.setString(2, p.getMaBenhNhan());
            stmtLichSuKham.setString(3, p.getMaBacSi());
            // Dùng cùng một ngày (vì bạn nói NgayKeDon = NgayGioKham)
            stmtLichSuKham.setTimestamp(4, new java.sql.Timestamp(p.getNgayKeDon().getTime()));
            stmtLichSuKham.executeUpdate();

            // --- KẾT THÚC TRANSACTION (COMMIT) ---
            conn.commit(); 
            System.out.println("Thêm đơn thuốc thành công!");
            
        } catch (Exception e) {
            // --- GẶP LỖI: HỦY BỎ TRANSACTION (ROLLBACK) ---
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction đã được rollback.");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // --- 4. Dọn dẹp tài nguyên ---
            try {
                if (stmtDonThuoc != null) stmtDonThuoc.close();
                if (stmtLichSuKham != null) stmtLichSuKham.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Trả lại trạng thái tự động commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lấy danh sách TẤT CẢ đơn thuốc (đã JOIN)
     * với các thông tin bạn yêu cầu.
     */
    public ArrayList<Prescription> getDanhSachDonThuocView() {
        ArrayList<Prescription> list = new ArrayList<>();
        
        // Câu lệnh SQL JOIN 2 bảng
        // Lấy NgayGioKham từ LichSuKham làm NgayKeDon
        String sql = "SELECT dt.MaDon, ls.MaBenhNhan, ls.MaBacSi, dt.NgayKeDon, dt.LieuDung " +
                     "FROM DonThuoc dt " +
                     "JOIN LichSuKham ls ON dt.MaDon = ls.MaDon";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maDon = rs.getString("MaDon");
                String maBenhNhan = rs.getString("MaBenhNhan");
                String maBacSi = rs.getString("MaBacSi");
                Date ngayKeDon = rs.getTimestamp("NgayKeDon"); // Lấy NgayGioKham
                String lieuDung = rs.getString("LieuDung");
                
                Prescription dtv = new Prescription(maDon, maBenhNhan, maBacSi, ngayKeDon, lieuDung);
                list.add(dtv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy thông tin chi tiết của CHỈ MỘT đơn thuốc
     * @param maDonCanTim Mã đơn thuốc bạn muốn tìm
     */
    public Prescription getDonThuocViewByMaDon(String maDonCanTim) {
        Prescription dtv = null;
        
        String sql = "SELECT dt.MaDon, ls.MaBenhNhan, ls.MaBacSi, ls.NgayGioKham, dt.LieuDung " +
                     "FROM DonThuoc dt " +
                     "JOIN LichSuKham ls ON dt.MaDon = ls.MaDon " +
                     "WHERE dt.MaDon = ?";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, maDonCanTim);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maDon = rs.getString("MaDon");
                    String maBenhNhan = rs.getString("MaBenhNhan");
                    String maBacSi = rs.getString("MaBacSi");
                    Date ngayKeDon = rs.getTimestamp("NgayGioKham"); // Lấy NgayGioKham
                    String lieuDung = rs.getString("LieuDung");
                    dtv = new Prescription(maDon, maBenhNhan, maBacSi, ngayKeDon, lieuDung);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtv;
    }
    
    // thêm một đơn thuốc mới
    public void updatePrescription(Prescription p) {
    String sqlDonThuoc = "UPDATE DonThuoc SET MaBacSi = ?, LieuDung = ?, NgayKeDon = ? WHERE MaDon = ?";
    String sqlLichSuKham = "UPDATE LichSuKham SET MaBenhNhan = ?, MaBacSi = ?, NgayGioKham = ? WHERE MaDon = ?";
    
    Connection conn = null;
    try {
        conn = new connectMysql().getConnection();
        conn.setAutoCommit(false); // Bắt đầu Transaction

        // 1. Cập nhật bảng DonThuoc
        try (PreparedStatement stmt = conn.prepareStatement(sqlDonThuoc)) {
            stmt.setString(1, p.getMaBacSi());
            stmt.setString(2, p.getLieuDung());
            stmt.setTimestamp(3, new java.sql.Timestamp(p.getNgayKeDon().getTime()));
            stmt.setString(4, p.getMaDon());
            stmt.executeUpdate();
        }

        // 2. Cập nhật bảng LichSuKham
        try (PreparedStatement stmt = conn.prepareStatement(sqlLichSuKham)) {
            stmt.setString(1, p.getMaBenhNhan());
            stmt.setString(2, p.getMaBacSi());
            stmt.setTimestamp(3, new java.sql.Timestamp(p.getNgayKeDon().getTime()));
            stmt.setString(4, p.getMaDon());
            stmt.executeUpdate();
        }
        
        conn.commit(); // Hoàn tất Transaction
        System.out.println("Cập nhật đơn thuốc thành công!");

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback(); // Hủy bỏ nếu có lỗi
        } catch (SQLException se) {
            se.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Xóa một đơn thuốc.
 * Sẽ xóa ở cả 2 bảng (bắt đầu từ bảng con 'DonThuoc' trước).
 * Dùng Transaction để đảm bảo an toàn.
 * (Lưu ý: Bảng LoaiThuoc cũng tham chiếu MaDon, bạn cần xóa ở đó trước,
 * hoặc cài đặt Foreign Key ON DELETE CASCADE)
 */
public void deletePrescription(String maDon) {
    // Giả sử LoaiThuoc và DonThuoc là bảng con của LichSuKham
    // Bạn phải xóa từ các bảng con trước
    String sqlLoaiThuoc = "DELETE FROM LoaiThuoc WHERE MaDon = ?";
    String sqlDonThuoc = "DELETE FROM DonThuoc WHERE MaDon = ?";
    String sqlLichSuKham = "DELETE FROM LichSuKham WHERE MaDon = ?";

    Connection conn = null;
    try {
        conn = new connectMysql().getConnection();
        conn.setAutoCommit(false); // Bắt đầu Transaction

        // 1. (QUAN TRỌNG) Xóa ở bảng LoaiThuoc
        try (PreparedStatement stmt = conn.prepareStatement(sqlLoaiThuoc)) {
            stmt.setString(1, maDon);
            stmt.executeUpdate();
        }
        
        // 2. Xóa ở bảng DonThuoc
        try (PreparedStatement stmt = conn.prepareStatement(sqlDonThuoc)) {
            stmt.setString(1, maDon);
            stmt.executeUpdate();
        }

        // 3. Xóa ở bảng LichSuKham
        try (PreparedStatement stmt = conn.prepareStatement(sqlLichSuKham)) {
            stmt.setString(1, maDon);
            stmt.executeUpdate();
        }

        conn.commit(); // Hoàn tất Transaction
        System.out.println("Xóa đơn thuốc thành công!");

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback(); // Hủy bỏ nếu có lỗi
        } catch (SQLException se) {
            se.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
}