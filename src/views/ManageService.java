package views;

// 1. IMPORT
import models.Service;          // Model của nhóm
import connection.connectMysql; // Connection của nhóm
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

        // Cho phép nhập Mã DV khi thêm mới
        maDV.setEnabled(true);
    }

    // 4. CÁC HÀM LOGIC VÀ SQL

    // --- Phần giao diện ---
    private void initTableService() {
        // 'jTable1' là tên JTable của bạn
        tableModel = (DefaultTableModel) jTable2.getModel();
        tableModel.setColumnCount(0);
        tableModel.addColumn("Mã Dịch Vụ");
        tableModel.addColumn("Tên Dịch Vụ");
        tableModel.addColumn("Chi Phí");
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void attachListenersService() {
        // Dùng tên biến nút BẠN ĐẶT (themDV, suaDV...)
        themDV.addActionListener(e -> handleAddService());
        suaDV.addActionListener(e -> handleUpdateService());
        xoaDV.addActionListener(e -> handleDeleteService());
        lamMoiDV.addActionListener(e -> clearFieldsService());

        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jTable2.getSelectedRow();
                if (row >= 0) {
                    // Dùng tên biến ô text BẠN ĐẶT (maDV, tenDV, chiPhi)
                    maDV.setText(tableModel.getValueAt(row, 0).toString());
                    tenDV.setText(tableModel.getValueAt(row, 1).toString());
                    chiPhi.setText(tableModel.getValueAt(row, 2).toString());
                    maDV.setEnabled(false); // Khóa Mã DV khi chọn từ bảng
                }
            }
        });
    }

    private void clearFieldsService() {
        // Dùng tên biến ô text BẠN ĐẶT
        maDV.setText("");
        tenDV.setText("");
        chiPhi.setText("");
        maDV.setEnabled(true); // Mở lại Mã DV để thêm mới
        jTable2.clearSelection();
    }

    // --- Phần xử lý nút bấm ---
    private void handleAddService() {
        try {
            Service s = new Service();
            // Dùng tên biến ô text BẠN ĐẶT
            s.setServiceId(maDV.getText().trim());
            s.setServiceName(tenDV.getText().trim());
            s.setPrice(Double.parseDouble(chiPhi.getText().trim()));
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
            String maDVValue = maDV.getText().trim();
            if (maDVValue.isEmpty() || maDV.isEnabled()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ từ bảng để sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Service s = new Service();
            s.setServiceId(maDVValue);
            s.setServiceName(tenDV.getText().trim());
            s.setPrice(Double.parseDouble(chiPhi.getText().trim()));
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
        String maDVValue = maDV.getText().trim();
        if (maDVValue.isEmpty() || maDV.isEnabled()) {
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

    // --- PHẦN CODE SQL (Chỉ dùng 3 cột chính của bảng dichvuyte) ---

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
        // Chỉ lấy 3 cột cần thiết (khớp với Model)
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
        // Chỉ INSERT 3 cột (khớp với Model)
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
        // Chỉ UPDATE Tên và Chi Phí
        String sql = "UPDATE dichvuyte SET tendichvu = ?, chiphi = ? WHERE madichvu = ?";
        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getServiceName());
            pstmt.setDouble(2, s.getPrice());
            pstmt.setString(3, s.getServiceId()); // Khóa chính ở cuối

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

        maDV = new javax.swing.JLabel();
        tenDV = new javax.swing.JLabel();
        chiPhi = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        themDV = new javax.swing.JButton();
        suaDV = new javax.swing.JButton();
        xoaDV = new javax.swing.JButton();
        lamMoiDV = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        maDV.setText("Mã Dịch Vụ");

        tenDV.setText("Tên Dịch Vụ");

        chiPhi.setText("Chi Phí");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        themDV.setText("Thêm ");
        themDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themDVActionPerformed(evt);
            }
        });

        suaDV.setText("Sửa");
        suaDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaDVActionPerformed(evt);
            }
        });

        xoaDV.setText("Xóa");
        xoaDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoaDVActionPerformed(evt);
            }
        });

        lamMoiDV.setText("Làm mới");
        lamMoiDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lamMoiDVActionPerformed(evt);
            }
        });

        jTextField4.setText("           BẢNG DỊCH VỤ Y TẾ");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tenDV, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chiPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maDV, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(themDV, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(xoaDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(suaDV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lamMoiDV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
                .addGap(425, 425, 425))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1069, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maDV, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themDV, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(suaDV, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tenDV, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chiPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xoaDV, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lamMoiDV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void themDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_themDVActionPerformed

    private void suaDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suaDVActionPerformed

    private void xoaDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoaDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xoaDVActionPerformed

    private void lamMoiDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lamMoiDVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lamMoiDVActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel chiPhi;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton lamMoiDV;
    private javax.swing.JLabel maDV;
    private javax.swing.JButton suaDV;
    private javax.swing.JLabel tenDV;
    private javax.swing.JButton themDV;
    private javax.swing.JButton xoaDV;
    // End of variables declaration//GEN-END:variables

    
}
