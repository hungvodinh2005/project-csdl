package views;

// 1. IMPORT
import models.Service;
import connection.connectMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;

public class ManageService extends javax.swing.JPanel {

    // 2. BIẾN
    private DefaultTableModel tableModel;

    // 3. CONSTRUCTOR
    public ManageService() {
        initComponents(); // Dòng này NetBeans tự tạo

        // Code của chúng ta
        initTableService();
        loadTableDataService();
        attachListenersService();

        // Cho phép nhập Mã DV khi thêm mới (Dùng tên biến ô text NetBeans)
        jTextField1.setEnabled(true);
    }

    // 4. CÁC HÀM LOGIC VÀ SQL

    // --- Phần giao diện ---
    private void initTableService() {
        // 'jtblServices' là tên JTable bạn đặt ở "Design"
        tableModel = (DefaultTableModel) jtblServices.getModel();
        tableModel.setColumnCount(0);
        tableModel.addColumn("Mã Dịch Vụ");
        tableModel.addColumn("Tên Dịch Vụ");
        tableModel.addColumn("Chi Phí");
        jtblServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void attachListenersService() {
        // Dùng tên biến nút NetBeans (jButton1, jButton2...)
        jButton1.addActionListener(e -> handleAddService()); // Nút Thêm
        jButton2.addActionListener(e -> handleUpdateService()); // Nút Sửa
        jButton3.addActionListener(e -> handleDeleteService()); // Nút Xóa
        jButton4.addActionListener(e -> clearFieldsService()); // Nút Làm mới

        jtblServices.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jtblServices.getSelectedRow();
                if (row >= 0) {
                    // Dùng tên biến ô text NetBeans (jTextField1, jTextField2...)
                    jTextField1.setText(tableModel.getValueAt(row, 0).toString()); // Mã DV
                    jTextField2.setText(tableModel.getValueAt(row, 1).toString()); // Tên DV
                    jTextField3.setText(tableModel.getValueAt(row, 2).toString()); // Chi Phí
                    jTextField1.setEnabled(false); // Khóa Mã DV khi chọn từ bảng
                }
            }
        });
    }

    private void clearFieldsService() {
        // Dùng tên biến ô text NetBeans
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField1.setEnabled(true); // Mở lại Mã DV để thêm mới
        jtblServices.clearSelection();
    }

    // --- Phần xử lý nút bấm ---
    private void handleAddService() {
        try {
            Service s = new Service();
            // Dùng tên biến ô text NetBeans
            s.setServiceId(jTextField1.getText().trim());
            s.setServiceName(jTextField2.getText().trim());
            s.setPrice(Double.parseDouble(jTextField3.getText().trim()));
            // Không setMaBenhNhan

            if (s.getServiceId().isEmpty() || s.getServiceName().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã và Tên Dịch Vụ là bắt buộc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = addServiceToDatabase(s);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                loadTableDataService();
                clearFieldsService();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm (Mã Dịch Vụ có thể đã tồn tại?).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Chi phí phải là một con số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }

    private void handleUpdateService() {
        try {
            String maDVValue = jTextField1.getText().trim();
            if (maDVValue.isEmpty() || jTextField1.isEnabled()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ từ bảng để sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Service s = new Service();
            s.setServiceId(maDVValue);
            s.setServiceName(jTextField2.getText().trim());
            s.setPrice(Double.parseDouble(jTextField3.getText().trim()));
             // Không setMaBenhNhan

            if (s.getServiceName().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Tên Dịch Vụ là bắt buộc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            boolean success = updateServiceInDatabase(s);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadTableDataService();
                clearFieldsService();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Chi phí phải là một con số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }

    private void handleDeleteService() {
        String maDVValue = jTextField1.getText().trim();
        if (maDVValue.isEmpty() || jTextField1.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ từ bảng để xóa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dịch vụ '" + maDVValue + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = deleteServiceFromDatabase(maDVValue);
        if (success) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadTableDataService();
            clearFieldsService();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa (Dịch vụ này có thể đang được sử dụng ở đâu đó?).", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- PHẦN CODE SQL (Giữ nguyên) ---

    private void loadTableDataService() {
        List<Service> serviceList = getAllServicesFromDatabase();
        tableModel.setRowCount(0);

        for (Service s : serviceList) {
            Vector<Object> row = new Vector<>();
            row.add(s.getServiceId());
            row.add(s.getServiceName());
            row.add(s.getPrice());
            tableModel.addRow(row);
        }
    }

    private List<Service> getAllServicesFromDatabase() {
        List<Service> serviceList = new ArrayList<>();
        String sql = "SELECT madichvu, tendichvu, chiphi FROM dichvuyte";

        try (Connection conn = new connectMysql().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getString("madichvu"));
                s.setServiceName(rs.getString("tendichvu"));
                s.setPrice(rs.getDouble("chiphi"));
                serviceList.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return serviceList;
    }

    private boolean addServiceToDatabase(Service s) {
        String sql = "INSERT INTO dichvuyte (madichvu, tendichvu, chiphi) VALUES (?, ?, ?)";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getServiceId());
            pstmt.setString(2, s.getServiceName());
            pstmt.setDouble(3, s.getPrice());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private boolean updateServiceInDatabase(Service s) {
        String sql = "UPDATE dichvuyte SET tendichvu = ?, chiphi = ? WHERE madichvu = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getServiceName());
            pstmt.setDouble(2, s.getPrice());
            pstmt.setString(3, s.getServiceId());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private boolean deleteServiceFromDatabase(String serviceId) {
        String sql = "DELETE FROM dichvuyte WHERE madichvu = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblServices = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();

        jLabel1.setText("Mã Dịch Vụ");

        jLabel2.setText("Tên Dịch Vụ");

        jLabel3.setText("Chi Phí");

        jButton1.setText("Thêm ");

        jButton2.setText("Sửa");

        jButton3.setText("Xóa");

        jTextField1.setText("maDV");

        jTextField2.setText("tenDV");

        jTextField3.setText("chiPhi");

        jtblServices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblServices);

        jButton4.setText("Làm mới");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1))
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1)
                        .addComponent(jTextField2))
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButton3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTable jtblServices;
    // End of variables declaration//GEN-END:variables

}