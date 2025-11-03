package views;

// 1. IMPORT
import models.Patient;
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

public class ManagePatients extends javax.swing.JPanel {

    // 2. BIẾN
    private DefaultTableModel tableModel;

    // 3. CONSTRUCTOR
    public ManagePatients() {
        initComponents(); // Dòng này NetBeans tự tạo

        // Code của chúng ta
        initTable();
        loadTableData();
        attachListeners();

        // Khóa ô text khi mới mở (Giả định jTextField1 là Mã BN, jTextField2 là CCCD)
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);
    }

    // 4. CÁC HÀM LOGIC VÀ SQL

    // --- Phần giao diện ---
    private void initTable() {
        // 'jTable1' là tên JTable trong Navigator
        tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setColumnCount(0);
        tableModel.addColumn("Mã Bệnh Nhân");
        tableModel.addColumn("CCCD");
        tableModel.addColumn("Họ Tên");
        tableModel.addColumn("Mã NGH");
        tableModel.addColumn("Tiền Sử Bệnh Án");
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void attachListeners() {
        // Dùng tên biến nút từ Navigator (jButton1, jButton2...)
        jButton1.addActionListener(e -> handleAddPatient()); // Giả định jButton1 là nút Thêm
        jButton2.addActionListener(e -> handleUpdatePatient()); // Giả định jButton2 là nút Sửa
        jButton3.addActionListener(e -> handleDeletePatient()); // Giả định jButton3 là nút Xóa
        jButton5.addActionListener(e -> handleSearchPatient()); // Giả định jButton5 là nút Tìm Kiếm
        jButton4.addActionListener(e -> { clearFields(); loadTableData(); }); // Giả định jButton4 là nút Làm Mới

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    // Dùng tên biến ô text từ Navigator (jTextField1, jTextField2...)
                    // !!! KIỂM TRA LẠI THỨ TỰ CÁC Ô TEXT !!!
                    jTextField1.setText(tableModel.getValueAt(selectedRow, 0).toString()); // Mã BN
                    jTextField2.setText(tableModel.getValueAt(selectedRow, 1).toString()); // CCCD
                    jTextField3.setText(tableModel.getValueAt(selectedRow, 2).toString()); // Họ Tên
                    jTextField4.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : ""); // NGH
                    jTextField5.setText(tableModel.getValueAt(selectedRow, 4) != null ? tableModel.getValueAt(selectedRow, 4).toString() : ""); // Tiền Sử

                    jTextField1.setEnabled(false);
                    jTextField2.setEnabled(false);
                }
            }
        });
    }

    private void clearFields() {
        // Dùng tên biến ô text từ Navigator
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);
        jTable1.clearSelection();
    }

    // --- Phần xử lý nút bấm ---
    private void handleAddPatient() {
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);

        Patient p = new Patient();
        // Dùng tên biến ô text từ Navigator
        p.setPatientId(jTextField1.getText().trim());
        p.setIdCard(jTextField2.getText().trim());
        p.setHoTen(jTextField3.getText().trim());
        p.setGuardianId(jTextField4.getText().trim());
        p.setMedicalHistory(jTextField5.getText().trim());

        if (p.getPatientId().isEmpty() || p.getIdCard().isEmpty() || p.getHoTen().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã BN, CCCD và Họ Tên là bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            return;
        }
        boolean success = addPatientToDatabase(p);
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm bệnh nhân thành công!");
            loadTableData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm (Mã BN/CCCD đã tồn tại, hoặc Mã NGH không đúng?).", "Lỗi", JOptionPane.ERROR_MESSAGE);
             jTextField1.setEnabled(false);
             jTextField2.setEnabled(false);
        }
    }

    private void handleUpdatePatient() {
        String maBNValue = jTextField1.getText().trim();
        if (maBNValue.isEmpty() || jTextField1.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân từ bảng để sửa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient p = new Patient();
        p.setPatientId(maBNValue);
        // Dùng tên biến ô text từ Navigator
        p.setIdCard(jTextField2.getText().trim());
        p.setHoTen(jTextField3.getText().trim());
        p.setGuardianId(jTextField4.getText().trim());
        p.setMedicalHistory(jTextField5.getText().trim());

        boolean success = updatePatientInDatabase(p);
        if (success) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTableData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeletePatient() {
        String maBNValue = jTextField1.getText().trim();
        String cccdValue = jTextField2.getText().trim();
        if (maBNValue.isEmpty() || jTextField1.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân từ bảng để xóa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bệnh nhân '" + maBNValue + "'? (Sẽ xóa cả 'Người' liên quan)", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = deletePatientFromDatabase(maBNValue, cccdValue);
        if (success) {
            JOptionPane.showMessageDialog(this, "Xóa bệnh nhân thành công!");
            loadTableData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa (Bệnh nhân này có thể đang liên kết với Lịch sử khám...)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearchPatient() {
        // Dùng tên biến ô text từ Navigator (Giả sử jTextField6 là ô tìm kiếm)
        String keyword = jTextField6.getText().trim();
        List<Patient> patientList = searchPatientsInDatabase(keyword);
        tableModel.setRowCount(0);
        for (Patient p : patientList) {
            Vector<String> row = new Vector<>();
            row.add(p.getPatientId());
            row.add(p.getIdCard());
            row.add(p.getHoTen());
            row.add(p.getGuardianId());
            row.add(p.getMedicalHistory());
            tableModel.addRow(row);
        }
    }

    // --- PHẦN CODE SQL (Giữ nguyên) ---

    private void loadTableData() {
        List<Patient> patientList = getAllPatientsFromDatabase();
        tableModel.setRowCount(0);

        for (Patient p : patientList) {
            Vector<String> row = new Vector<>();
            row.add(p.getPatientId());
            row.add(p.getIdCard());
            row.add(p.getHoTen());
            row.add(p.getGuardianId());
            row.add(p.getMedicalHistory());
            tableModel.addRow(row);
        }
    }

    private List<Patient> getAllPatientsFromDatabase() {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT b.mabenhnhan, b.cccd, n.hoten, b.ngh_cccd, b.tiensubenhan " +
                     "FROM benhnhan b JOIN nguoi n ON b.cccd = n.cccd";

        try (Connection conn = new connectMysql().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getString("mabenhnhan"));
                p.setIdCard(rs.getString("cccd"));
                p.setHoTen(rs.getString("hoten"));
                p.setGuardianId(rs.getString("ngh_cccd"));
                p.setMedicalHistory(rs.getString("tiensubenhan"));
                patientList.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return patientList;
    }

    private boolean addPatientToDatabase(Patient p) {
        Connection conn = null;
        PreparedStatement pstmtNguoi = null, pstmtBenhNhan = null;
        String sqlNguoi = "INSERT INTO nguoi (cccd, hoten) VALUES (?, ?)";
        String sqlBenhNhan = "INSERT INTO benhnhan (mabenhnhan, cccd, ngh_cccd, tiensubenhan) VALUES (?, ?, ?, ?)";

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false);

            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, p.getIdCard());
            pstmtNguoi.setString(2, p.getHoTen());
            pstmtNguoi.executeUpdate();

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, p.getPatientId());
            pstmtBenhNhan.setString(2, p.getIdCard());
            pstmtBenhNhan.setString(3, p.getGuardianId());
            pstmtBenhNhan.setString(4, p.getMedicalHistory());
            pstmtBenhNhan.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private boolean updatePatientInDatabase(Patient p) {
         Connection conn = null;
        PreparedStatement pstmtNguoi = null, pstmtBenhNhan = null;
        String sqlNguoi = "UPDATE nguoi SET hoten = ? WHERE cccd = ?";
        String sqlBenhNhan = "UPDATE benhnhan SET ngh_cccd = ?, tiensubenhan = ? WHERE mabenhnhan = ?";

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false);

            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, p.getHoTen());
            pstmtNguoi.setString(2, p.getIdCard());
            pstmtNguoi.executeUpdate();

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, p.getGuardianId());
            pstmtBenhNhan.setString(2, p.getMedicalHistory());
            pstmtBenhNhan.setString(3, p.getPatientId());
            pstmtBenhNhan.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
             try {
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private boolean deletePatientFromDatabase(String patientId, String cccd) {
        Connection conn = null;
        PreparedStatement pstmtBenhNhan = null, pstmtNguoi = null;
        String sqlBenhNhan = "DELETE FROM benhnhan WHERE mabenhnhan = ?";
        String sqlNguoi = "DELETE FROM nguoi WHERE cccd = ?";

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false);

            // LƯU Ý: Cần xóa các bảng con (như LichSuKham) trước nếu có khóa ngoại

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, patientId);
            pstmtBenhNhan.executeUpdate();

            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, cccd);
            pstmtNguoi.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
             try {
                if (pstmtBenhNhan != null) pstmtBenhNhan.close();
                if (pstmtNguoi != null) pstmtNguoi.close();
                if (conn != null) { conn.setAutoCommit(true); conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private List<Patient> searchPatientsInDatabase(String keyword) {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT b.mabenhnhan, b.cccd, n.hoten, b.ngh_cccd, b.tiensubenhan " +
                     "FROM benhnhan b JOIN nguoi n ON b.cccd = n.cccd " +
                     "WHERE n.hoten LIKE ? OR b.cccd LIKE ? OR b.mabenhnhan LIKE ?";

        try (Connection conn = new connectMysql().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + keyword + "%";
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient();
                    p.setPatientId(rs.getString("mabenhnhan"));
                    p.setIdCard(rs.getString("cccd"));
                    p.setHoTen(rs.getString("hoten"));
                    p.setGuardianId(rs.getString("ngh_cccd"));
                    p.setMedicalHistory(rs.getString("tiensubenhan"));
                    patientList.add(p);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return patientList;
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        Patient = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setText("Mã BN");

        jLabel2.setText("CCCD");

        jLabel3.setText("Họ Tên");

        jLabel4.setText("NGH");

        jLabel5.setText("Tiền Sử");

        jTextField1.setText("maBN");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setText("cccd");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setText("hoTen");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setText("ngh");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setText("tienSu");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Sửa");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xóa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Tên");

        jTextField6.setText("Tìm Kiếm");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton5.setText("Tìm Kiếm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        Patient.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(137, 137, 137))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(24, 24, 24)
                                                .addComponent(jButton5))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                                    .addComponent(jTextField4)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton1)
                                            .addComponent(jButton3)
                                            .addComponent(jButton4)
                                            .addComponent(jButton2))))
                                .addGap(0, 77, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Patient, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Patient, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    
     

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane Patient;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
