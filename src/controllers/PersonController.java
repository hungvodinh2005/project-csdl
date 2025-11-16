package controllers;

import connection.connectMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Person;

public class PersonController {

    // THÊM PERSON
    public void createPerson(Person p) {
        String sql = "INSERT INTO nguoi (cccd, hoten, ngaysinh, mabhyt, sdt, email, diachi) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getCccd());
            stmt.setString(2, p.getHoten());
            stmt.setDate(3, p.getNgaysinh());  
            stmt.setString(4, p.getMabhyt());
            stmt.setString(5, p.getSdt());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getDiachi());

            stmt.executeUpdate();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // HIỂN THỊ TẤT CẢ PERSON
    public ArrayList<Person> showAll() {
        ArrayList<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi";

        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Person p = new Person();
                p.setCccd(rs.getString("cccd"));
                p.setHoten(rs.getString("hoten"));
                p.setNgaysinh(rs.getDate("ngaysinh"));
                p.setMabhyt(rs.getString("mabhyt"));
                p.setSdt(rs.getString("sdt"));
                p.setEmail(rs.getString("email"));
                p.setDiachi(rs.getString("diachi"));
                list.add(p);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // SỬA PERSON
    public void editPerson(Person p) {
        String sql = "UPDATE nguoi SET hoten=?, ngaysinh=?, mabhyt=?, sdt=?, email=?, diachi=? "
                   + "WHERE cccd=?";

        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, p.getHoten());
            stmt.setDate(2, p.getNgaysinh());
            stmt.setString(3, p.getMabhyt());
            stmt.setString(4, p.getSdt());
            stmt.setString(5, p.getEmail());
            stmt.setString(6, p.getDiachi());
            stmt.setString(7, p.getCccd());

            stmt.executeUpdate();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // XÓA PERSON
  public void deletePerson(String cccd) {
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
        conn = new connectMysql().getConnection();

        // 1. Kiểm tra xem người này có phải bệnh nhân không
        String sqlCheck = "SELECT MaBenhNhan FROM benhnhan WHERE CCCD = ?";
        st = conn.prepareStatement(sqlCheck);
        st.setString(1, cccd);
        rs = st.executeQuery();

        while (rs.next()) {
            String maBN = rs.getString("MaBenhNhan"); // <-- VARCHAR

            // 2. Xóa dịch vụ y tế của bệnh nhân
            String sqlDeleteDV = "DELETE FROM dichvuyte WHERE MaBenhNhan = ?";
            PreparedStatement stDV = conn.prepareStatement(sqlDeleteDV);
            stDV.setString(1, maBN);
            stDV.executeUpdate();

            // 3. Xóa bệnh nhân
            String sqlDeleteBN = "DELETE FROM benhnhan WHERE MaBenhNhan = ?";
            PreparedStatement stBN = conn.prepareStatement(sqlDeleteBN);
            stBN.setString(1, maBN);
            stBN.executeUpdate();
        }

        // 4. Cuối cùng xóa người
        String sqlDeletePerson = "DELETE FROM nguoi WHERE CCCD = ?";
        PreparedStatement stP = conn.prepareStatement(sqlDeletePerson);
        stP.setString(1, cccd);
        stP.executeUpdate();

        System.out.println("Xóa người thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi xóa người!");
    }
}



    // KIỂM TRA CCCD TỒN TẠI
    public boolean isCCCDExists(String cccd) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM nguoi WHERE cccd=?";

        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    // TÌM THEO TÊN
    public ArrayList<Person> searchByName(String name) {
        ArrayList<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi WHERE hoten LIKE ?";

        try {
            Connection conn = new connectMysql().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Person p = new Person();
                p.setCccd(rs.getString("cccd"));
                p.setHoten(rs.getString("hoten"));
                p.setNgaysinh(rs.getDate("ngaysinh"));
                p.setMabhyt(rs.getString("mabhyt"));
                p.setSdt(rs.getString("sdt"));
                p.setEmail(rs.getString("email"));
                p.setDiachi(rs.getString("diachi"));
                list.add(p);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public String getHoTenByIdCard(String cccd) {
        String sql = "SELECT hoten FROM nguoi WHERE cccd = ?";
        String hoTen = null;

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cccd);
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
}
