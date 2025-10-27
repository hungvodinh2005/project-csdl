package controllers;

import java.sql.*;
import java.util.*;
import connection.connectMysql;
import models.Medicine;

public class MedicineController {
    
    //tim kiem thuoc
    public boolean isCardIdExists(String cardId) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM loaithuoc WHERE idCard = ?";
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
    //hien thi toan bo thuoc
    public ArrayList<Medicine> showAllMedicine() {
        ArrayList<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM loaithuoc";
        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement show = conn.prepareStatement(sql);
            ResultSet rs = show.executeQuery();
            while (rs.next()) {
                Medicine i = new Medicine();
                i.setMedicineID(rs.getString("MaThuoc"));
                i.setMedicineLoai(rs.getString("TenLoai"));
                i.setMedicineMaDon(rs.getString("MaDon"));
                i.setMedicineNsx(rs.getString("NhaSanXuat"));
                list.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Sinh mã thuốc tự động: M001, M002, ...
    public String generateMedicineID() {
        String newId = "LT001"; // Sửa prefix thành LT
        String sql = "SELECT MaThuoc FROM LoaiThuoc ORDER BY MaThuoc DESC LIMIT 1";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("MaThuoc");
                // Lấy số từ 2 ký tự prefix (LT)
                int number = Integer.parseInt(lastId.substring(2)) + 1; 
                newId = String.format("LT%03d", number); // Sửa prefix thành LT
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    // ✅ Thêm thuốc mới
    public void createMedicine(Medicine med) {
        // Sửa TenThuoc thành MaDon
        String sql = "INSERT INTO LoaiThuoc (MaThuoc, MaDon, TenLoai, NhaSanXuat) VALUES (?, ?, ?, ?)";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getMedicineID());
            stmt.setString(2, med.getMedicineLoai()); // Sửa thành MaDon
            stmt.setString(3, med.getMedicineMaDon());
            stmt.setString(4, med.getMedicineNsx());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Sửa thông tin thuốc
    public void updateMedicine(Medicine med) {
        // Sửa TenThuoc thành MaDon
        String sql = "UPDATE LoaiThuoc SET MaDon = ?, TenLoai = ?, NhaSanXuat = ? WHERE MaThuoc = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getMedicineID()); // Sửa thành MaDon
            stmt.setString(2, med.getMedicineLoai());
            stmt.setString(3, med.getMedicineMaDon());
            stmt.setString(4, med.getMedicineNsx());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Xóa thuốc theo mã
    public void deleteMedicine(String medicineId) {
        String sql = "DELETE FROM LoaiThuoc WHERE MaThuoc = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicineId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Kiểm tra trùng mã thuốc
    public boolean isMedicineIdExists(String medicineId) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM LoaiThuoc WHERE MaThuoc = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
}