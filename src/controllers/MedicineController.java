package controllers;

import java.sql.*;
import java.util.*;
import connection.connectMysql;
import models.Medicine;

public class MedicineController {

    // ✅ Sinh mã thuốc tự động: M001, M002, ...
    public String generateMedicineID() {
        String newId = "M001";
        String sql = "SELECT MaThuoc FROM LoaiThuoc ORDER BY MaThuoc DESC LIMIT 1";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("MaThuoc");
                int number = Integer.parseInt(lastId.substring(1)) + 1;
                newId = String.format("M%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    // ✅ Thêm thuốc mới
    public void createMedicine(Medicine med) {
        String sql = "INSERT INTO LoaiThuoc (MaThuoc, TenThuoc, TenLoai, NhaSanXuat) VALUES (?, ?, ?, ?)";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getMedicineId());
            stmt.setString(2, med.getMedicineName());
            stmt.setString(3, med.getCategory());
            stmt.setString(4, med.getManufacturer());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Lấy danh sách tất cả thuốc
    public ArrayList<Medicine> getAllMedicines() {
        ArrayList<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiThuoc";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicine med = new Medicine();
                med.setMedicineId(rs.getString("MaThuoc"));
                med.setMedicineName(rs.getString("TenThuoc"));
                med.setCategory(rs.getString("TenLoai"));
                med.setManufacturer(rs.getString("NhaSanXuat"));
                list.add(med);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Sửa thông tin thuốc
    public void updateMedicine(Medicine med) {
        String sql = "UPDATE LoaiThuoc SET TenThuoc = ?, TenLoai = ?, NhaSanXuat = ? WHERE MaThuoc = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, med.getMedicineName());
            stmt.setString(2, med.getCategory());
            stmt.setString(3, med.getManufacturer());
            stmt.setString(4, med.getMedicineId());
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

    // ✅ Tìm thuốc theo tên
    public ArrayList<Medicine> searchMedicineByName(String name) {
        ArrayList<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiThuoc WHERE TenThuoc LIKE ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medicine med = new Medicine();
                med.setMedicineId(rs.getString("MaThuoc"));
                med.setMedicineName(rs.getString("TenThuoc"));
                med.setCategory(rs.getString("TenLoai"));
                med.setManufacturer(rs.getString("NhaSanXuat"));
                list.add(med);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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