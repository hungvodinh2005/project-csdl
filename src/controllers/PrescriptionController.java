package controllers;

import java.sql.*;
import java.util.*;
import connection.connectMysql;
import models.Prescription;

public class PrescriptionController {

    // ✅ Sinh mã đơn thuốc tự động (D001, D002, ...)
    public String generatePrescriptionID() {
        String newId = "D001";
        String sql = "SELECT MaDon FROM DonThuoc ORDER BY MaDon DESC LIMIT 1";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("MaDon");
                int number = Integer.parseInt(lastId.substring(1)) + 1;
                newId = String.format("D%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    // ✅ Thêm đơn thuốc mới
    public void createPrescription(Prescription p) {
        String sql = "INSERT INTO DonThuoc (MaDon, MaBacSi, LieuDung, NgayKeDon) VALUES (?, ?, ?, ?)";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getPrescriptionId());
            stmt.setString(2, p.getDoctorId());
            stmt.setString(3, p.getDosage());
            stmt.setDate(4, p.getDate());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Lấy tất cả đơn thuốc
    public ArrayList<Prescription> getAllPrescriptions() {
        ArrayList<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM DonThuoc";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getString("MaDon"));
                p.setDoctorId(rs.getString("MaBacSi"));
                p.setDosage(rs.getString("LieuDung"));
                p.setDate(rs.getDate("NgayKeDon"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Sửa đơn thuốc
    public void updatePrescription(Prescription p) {
        String sql = "UPDATE DonThuoc SET MaBacSi = ?, LieuDung = ?, NgayKeDon = ? WHERE MaDon = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getDoctorId());
            stmt.setString(2, p.getDosage());
            stmt.setDate(3, p.getDate());
            stmt.setString(4, p.getPrescriptionId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Xóa đơn thuốc
    public void deletePrescription(String prescriptionId) {
        String sql = "DELETE FROM DonThuoc WHERE MaDon = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prescriptionId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Tìm đơn thuốc theo mã bác sĩ
    public ArrayList<Prescription> searchByDoctor(String doctorId) {
        ArrayList<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM DonThuoc WHERE MaBacSi = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getString("MaDon"));
                p.setDoctorId(rs.getString("MaBacSi"));
                p.setDosage(rs.getString("LieuDung"));
                p.setDate(rs.getDate("NgayKeDon"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Kiểm tra mã đơn thuốc đã tồn tại chưa
    public boolean isPrescriptionIdExists(String prescriptionId) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM DonThuoc WHERE MaDon = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prescriptionId);
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
