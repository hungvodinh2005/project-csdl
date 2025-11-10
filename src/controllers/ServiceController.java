package controllers;

import connection.JDBCUtil;
import connection.connectMysql; // Đảm bảo import đúng file kết nối
import models.Service; // Sửa thành models.Service
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceController {

    /**
     * Lấy tất cả dịch vụ từ CSDL
     */
    public List<Service> getAllServices() { // Sửa tên hàm và kiểu trả về
        List<Service> dsDichVu = new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        // Tên bảng CSDL vẫn là 'dichvu'
        String sql = "SELECT madichvu, tendichvu, chiphi FROM dichvu"; 

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                // Sửa thành đối tượng Service
                Service dv = new Service(
                    rs.getString("madichvu"),
                    rs.getString("tendichvu"),
                    rs.getDouble("chiphi")
                );
                dsDichVu.add(dv);
            }
        } catch (SQLException e) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {}
        }
        return dsDichVu;
    }

    /**
     * Thêm một dịch vụ mới
     */
    public int insert(Service dv) { // Sửa tham số
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "INSERT INTO dichvu (madichvu, tendichvu, chiphi) VALUES (?, ?, ?)";

        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, dv.getMaDichVu());
            pst.setString(2, dv.getTenDichVu());
            pst.setDouble(3, dv.getChiPhi());
            
            kq = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {}
        }
        return kq;
    }

    /**
     * Xóa một dịch vụ
     */
    public int delete(String maDichVu) {
        int kq = 0;
        Connection con = JDBCUtil.getConnection();
        String sql = "DELETE FROM dichvu WHERE madichvu = ?";

        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, maDichVu);
            kq = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {}
        }
        return kq;
    }
}