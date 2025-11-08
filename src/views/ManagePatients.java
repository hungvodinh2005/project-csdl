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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ListSelectionModel;
import models.Person;

public class ManagePatients extends javax.swing.JPanel {

    // 2. BIẾN
    private DefaultTableModel tableModel;
    // Không cần controller

    // 3. CONSTRUCTOR
    public ManagePatients() {
        initComponents(); // Dòng này NetBeans tự tạo

        
        initTable();
        loadTableData();
        attachListeners();

        // Khóa ô text khi mới mở
        jTextField1.setEnabled(true); // Ô Mã BN
        jTextField2.setEnabled(true); // Ô CCCD
    }

    // 4. CÁC HÀM LOGIC VÀ SQL

    // --- Phần giao diện ---
    private void initTable() {
        // 'jtable2' là tên JTable của bạn
        tableModel = (DefaultTableModel) jTable2.getModel();
        tableModel.setColumnCount(0);
        tableModel.addColumn("Mã Bệnh Nhân");
        tableModel.addColumn("CCCD");
        tableModel.addColumn("Họ Tên");
        tableModel.addColumn("Mã NGH");
        tableModel.addColumn("Tiền Sử Bệnh Án");
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void attachListeners() {
        // Dùng tên biến nút của bạn
        jButton5.addActionListener(e -> handleAddPatient());    // Nút Thêm
        jButton6.addActionListener(e -> handleUpdatePatient()); // Nút Sửa
        jButton7.addActionListener(e -> handleDeletePatient()); // Nút Xóa
        jButton8.addActionListener(e -> { clearFields(); loadTableData(); }); // Nút Làm mới
        jButton9.addActionListener(e -> handleSearchPatient()); // Nút Tìm Kiếm

        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = jTable2.getSelectedRow();
                if (selectedRow != -1) {
                    // Dùng tên biến ô text của bạn
                    jTextField1.setText(tableModel.getValueAt(selectedRow, 0).toString()); // Mã BN
                    jTextField2.setText(tableModel.getValueAt(selectedRow, 1).toString()); // CCCD
                    jTextField3.setText(tableModel.getValueAt(selectedRow, 2).toString()); // Họ Tên
                    jTextField4.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : ""); // NGH
                    jTextField5.setText(tableModel.getValueAt(selectedRow, 4) != null ? tableModel.getValueAt(selectedRow, 4).toString() : ""); // Tiền Sử

                    jTextField1.setEnabled(false); // Khóa Mã BN
                    jTextField2.setEnabled(false); // Khóa CCCD
                }
            }
        });
    }

    private void clearFields() {
        // Dùng tên biến ô text của bạn
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);
        jTable2.clearSelection();
    }

    // --- Phần xử lý nút bấm ---
    private void handleAddPatient() {
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);

        Patient p = new Patient();
        // Dùng tên biến ô text của bạn
        p.setPatientId(jTextField1.getText().trim());
        p.setIdCard(jTextField2.getText().trim());
        p.setHoTen(jTextField3.getText().trim());
        p.setGuardianId(jTextField4.getText().trim());
        
//        TODO: thêm trường birthDate, gender, phone, address, email
        Person person = new Person();
        person.setIdCard(jTextField2.getText().trim());
        person.setFullName(jTextField3.getText().trim());
//        person.setBirthDate(jTextField);

        if (p.getPatientId().isEmpty() || p.getIdCard().isEmpty() || p.getHoTen().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã BN, CCCD và Họ Tên là bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            return;
        }
        boolean success = addPatientToDatabase(p, person);
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
        // Dùng tên biến ô text của bạn
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
        // Dùng tên biến ô text của bạn (jTextField6 là ô tìm kiếm)
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
        String sql = "SELECT *" +
"FROM benhnhan b " +
"JOIN nguoi n " +
"ON b.cccd = n.cccd;";

        try {
            Connection conn = new connectMysql().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getString("mabenhnhan"));
                p.setIdCard(rs.getString("cccd"));
                p.setHoTen(rs.getString("hoten"));
                p.setGuardianId(rs.getString("manguoigiamho"));
                patientList.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return patientList;
    }

    private boolean addPatientToDatabase(Patient p, Person person) {
        Connection conn = null;
        PreparedStatement pstmtNguoi = null, pstmtBenhNhan = null;
//        TODO: sửa lại câu sql để thêm thông tin birthDate, gender, phone, address, email
        String sqlNguoi = "INSERT INTO nguoi (cccd, hoten) VALUES (?, ?)";
        String sqlBenhNhan = "INSERT INTO benhnhan (mabenhnhan, cccd, manguoigiamho, ngaytiepnhan) VALUES (?, ?, ?, ?)";

        try {
            conn = new connectMysql().getConnection();
            conn.setAutoCommit(false);

            pstmtNguoi = conn.prepareStatement(sqlNguoi);
            pstmtNguoi.setString(1, person.getIdCard());
            pstmtNguoi.setString(2, person.getFullName());
//          TODO: set thông tin cho câu sql
//            pstmtNguoi.setString(3, person.getBirthDate())
            pstmtNguoi.executeUpdate();

            pstmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            pstmtBenhNhan.setString(1, p.getPatientId());
            pstmtBenhNhan.setString(2, p.getIdCard());
            pstmtBenhNhan.setString(3, p.getGuardianId());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            pstmtBenhNhan.setString(4, formattedDateTime);
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

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

        jLabel4.setText("Mã BN");

        jLabel5.setText("CCCD");

        jLabel6.setText("Họ Tên");

        jLabel7.setText("NGH");

        jLabel8.setText("Tiền Sử");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton5.setText("Thêm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Sửa");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Làm mới");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Xóa");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel9.setText("Tên");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton9.setText("Tìm Kiếm");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTextField7.setText("BỆNH NHÂN");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
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
        jScrollPane1.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(285, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

    
}

