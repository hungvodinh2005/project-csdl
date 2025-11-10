/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

/**
 *
 * @author phams
 */

import controllers.PrescriptionController;
import models.Thuoc;

import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import models.Prescription;


public class ManagePrescription extends javax.swing.JPanel {

    /**
     * Creates new form ManagePrescription
     */
    
    private PrescriptionController prescriptionController;
    private javax.swing.table.DefaultTableModel tableModel;
    private java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private javax.swing.table.DefaultTableModel tableModelChiTiet;
    private java.util.List<models.Thuoc> dsTatCaThuoc;
//    Tải (làm mới) toàn bộ dữ liệu lên JTable
    private void loadTableData() {
        tableModel.setRowCount(0); 
        try {
            java.util.List<Prescription> list = prescriptionController.getAllPrescriptions();
            
            for (Prescription p : list) {
                tableModel.addRow(new Object[]{
                    p.getMaHoSo(),
                    p.getMaDon(),
                    df.format(p.getNgayKeDon()),
                    p.getMaBacSi()
                });
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu bảng: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
// Xóa trắng form và tạo mã đơn mới
    private void clearForm() {
        try {
            txtMaDon.setText(prescriptionController.nextPrescriptionID());
            txtNgayKeDon.setText(df.format(new java.util.Date()));
            if (comboMaHoSo.getItemCount() > 0) {
                comboMaHoSo.setSelectedIndex(0);
            }
            if (comboMaBacSi.getItemCount() > 0) {
                comboMaBacSi.setSelectedIndex(0);
            }
            tblDonThuoc.clearSelection();
            
        } catch (Exception e) {
            txtMaDon.setText("Lỗi tạo mã");
            e.printStackTrace();
        }
    }
    
    private void loadChiTietTable(String maDon) {
        tableModelChiTiet.setRowCount(0); // Xóa bảng chi tiết cũ
        
        try {
            List<models.ChiTietDonThuoc> list = prescriptionController.getChiTietDonThuoc(maDon);
            
            for (models.ChiTietDonThuoc ct : list) {
                tableModelChiTiet.addRow(new Object[] {
                    ct.getMaDon(),
                    ct.getThuoc().getMaThuoc(), // Lấy Mã Thuốc
                    ct.getSoLuong(),
                    ct.getGia(), // Giá tổng
                    ct.getLieuDung()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết đơn thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadInitialData() {
        try {
            // 1. Tải Mã Đơn và Ngày Kê Đơn (Tạo mới)
            txtMaDon.setText(prescriptionController.nextPrescriptionID());
            txtNgayKeDon.setText(df.format(new java.util.Date()));
            txtNgayKeDon.setEditable(false); // Không cho sửa
            
            // 2. Tải Mã Hồ Sơ (LẤY CÓ SẴN)
            List<String> recordIDs = prescriptionController.getAllMedicalRecordIDs();
            comboMaHoSo.removeAllItems(); // Xóa các mục cũ
            for (String id : recordIDs) {
                comboMaHoSo.addItem(id);
            }
            
            // 3. Tải Mã Bác Sĩ (LẤY CÓ SẴN)
            List<String> doctorIDs = prescriptionController.getAllDoctorIDs();
            comboMaBacSi.removeAllItems(); // Xóa các mục cũ
            for (String id : doctorIDs) {
                comboMaBacSi.addItem(id);
            }
            
            // 4. Tải Danh Sách Thuốc (cho comboMaThuoc_CT)
            dsTatCaThuoc = prescriptionController.getAllThuoc(); 
            comboMaThuoc_CT.removeAllItems();
            models.Thuoc placeholder = new models.Thuoc();
            placeholder.setTenThuoc("-- Chọn thuốc --");
            // (Các trường khác của nó sẽ là null hoặc 0)
            
            comboMaThuoc_CT.addItem(placeholder); // Thêm đối tượng "giả"
            
            for (models.Thuoc t : dsTatCaThuoc) {
                comboMaThuoc_CT.addItem(t); // Thêm TOÀN BỘ đối tượng Thuoc
            }
            
            // 5. Tải Danh Sách Mã Đơn (cho comboMaDon_CT)
            List<String> donIDs = prescriptionController.getAllPrescriptionIDs();
            comboMaDon_CT.removeAllItems();
            comboMaDon_CT.addItem("-- Chọn đơn thuốc --"); // Thêm mục mặc định
            for (String id : donIDs) {
                comboMaDon_CT.addItem(id);
            }
            
            // 6. Khóa các ô tự động điền
            txtTenThuoc_CT.setEditable(false);
            txtGia_CT.setEditable(false);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị lỗi nếu không tải được JComboBox
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách Hồ sơ/Bác sĩ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tblDonThuocMousePressed(java.awt.event.MouseEvent evt) {                                         
        int row = tblDonThuoc.getSelectedRow();
        
        if (row >= 0) {
            // Lấy dữ liệu từ TableModel
            String maHoSo = tableModel.getValueAt(row, 0).toString();
            String maDon = tableModel.getValueAt(row, 1).toString();
            String ngayKeDon = tableModel.getValueAt(row, 2).toString();
            String maBacSi = tableModel.getValueAt(row, 3).toString();
            
            // Đổ dữ liệu lên các ô nhập
            txtMaDon.setText(maDon);
            txtNgayKeDon.setText(ngayKeDon);
            comboMaHoSo.setSelectedItem(maHoSo);
            comboMaBacSi.setSelectedItem(maBacSi);
        }
    }
    
    public ManagePrescription() {
        initComponents();
        this.prescriptionController = new PrescriptionController();
        this.tableModel = (DefaultTableModel) tblDonThuoc.getModel();
//        khởi tạo cho tab 2 
        this.tableModelChiTiet = (DefaultTableModel) tblChiTiet.getModel(); // Tên JTable ở Tab 2
        this.dsTatCaThuoc = new java.util.ArrayList<>();
        loadInitialData(); 
        loadTableData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox<>();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboMaHoSo = new javax.swing.JComboBox<>();
        comboMaBacSi = new javax.swing.JComboBox<>();
        txtMaDon = new javax.swing.JTextField();
        txtNgayKeDon = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnHienThi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDonThuoc = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        comboMaDon_CT = new javax.swing.JComboBox<>();
        comboMaThuoc_CT = new javax.swing.JComboBox<>();
        txtSoLuong_CT = new javax.swing.JTextField();
        txtLieuDung_CT = new javax.swing.JTextField();
        txtGia_CT = new javax.swing.JTextField();
        txtTenThuoc_CT = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTiet = new javax.swing.JTable();
        btnThem_CT = new javax.swing.JButton();
        btnXoa_CT = new javax.swing.JButton();

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        setPreferredSize(new java.awt.Dimension(1360, 750));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBackground(new java.awt.Color(45, 55, 72));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mã Đơn");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mã Hồ Sơ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Ngày kê đơn");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Mã Bác Sĩ");

        comboMaHoSo.setBackground(new java.awt.Color(74, 85, 104));
        comboMaHoSo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaHoSoActionPerformed(evt);
            }
        });

        comboMaBacSi.setBackground(new java.awt.Color(74, 85, 104));
        comboMaBacSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaBacSiActionPerformed(evt);
            }
        });

        txtMaDon.setBackground(new java.awt.Color(74, 85, 104));
        txtMaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaDonActionPerformed(evt);
            }
        });

        txtNgayKeDon.setBackground(new java.awt.Color(74, 85, 104));
        txtNgayKeDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayKeDonActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(56, 178, 172));
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(56, 178, 172));
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnHienThi.setBackground(new java.awt.Color(56, 178, 172));
        btnHienThi.setForeground(new java.awt.Color(255, 255, 255));
        btnHienThi.setText("Hiện thị");
        btnHienThi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiActionPerformed(evt);
            }
        });

        tblDonThuoc.setBackground(new java.awt.Color(74, 85, 104));
        tblDonThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Hồ Sơ", "Mã Đơn", "Ngày Kê Đơn", "Mã Bác Sĩ"
            }
        ));
        jScrollPane1.setViewportView(tblDonThuoc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNgayKeDon, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboMaHoSo, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaDon)
                            .addComponent(comboMaBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(89, 89, 89)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHienThi)
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoa))))
                .addContainerGap(129, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comboMaBacSi, comboMaHoSo, txtMaDon, txtNgayKeDon});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHienThi, btnThem, btnXoa});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboMaHoSo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(btnXoa))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtMaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addComponent(btnHienThi))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboMaBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtNgayKeDon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {comboMaBacSi, comboMaHoSo, txtMaDon, txtNgayKeDon});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnHienThi, btnThem, btnXoa});

        jTabbedPane2.addTab("Đơn Thuốc", jPanel2);

        jPanel1.setBackground(new java.awt.Color(45, 55, 72));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Tên thuốc");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Giá");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Liều dùng");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Số lượng");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Mã Thuốc");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Mã Đơn");

        comboMaDon_CT.setBackground(new java.awt.Color(74, 85, 104));
        comboMaDon_CT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaDon_CTActionPerformed(evt);
            }
        });

        comboMaThuoc_CT.setBackground(new java.awt.Color(74, 85, 104));
        comboMaThuoc_CT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaThuoc_CTActionPerformed(evt);
            }
        });

        txtSoLuong_CT.setBackground(new java.awt.Color(74, 85, 104));

        txtLieuDung_CT.setBackground(new java.awt.Color(74, 85, 104));

        txtGia_CT.setBackground(new java.awt.Color(74, 85, 104));

        txtTenThuoc_CT.setEditable(false);
        txtTenThuoc_CT.setBackground(new java.awt.Color(74, 85, 104));

        tblChiTiet.setBackground(new java.awt.Color(74, 85, 104));
        tblChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Đơn", "Mã Thuốc", "Số Lượng", "Giá", "Liều Dùng"
            }
        ));
        tblChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblChiTietMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblChiTiet);

        btnThem_CT.setBackground(new java.awt.Color(56, 178, 172));
        btnThem_CT.setForeground(new java.awt.Color(255, 255, 255));
        btnThem_CT.setText("Thêm");
        btnThem_CT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem_CTActionPerformed(evt);
            }
        });

        btnXoa_CT.setBackground(new java.awt.Color(56, 178, 172));
        btnXoa_CT.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa_CT.setText("Xóa");
        btnXoa_CT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa_CTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnThem_CT)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSoLuong_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(comboMaThuoc_CT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboMaDon_CT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenThuoc_CT, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(txtGia_CT)
                            .addComponent(txtLieuDung_CT))
                        .addGap(35, 35, 35))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnXoa_CT)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboMaDon_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtTenThuoc_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(comboMaThuoc_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtGia_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSoLuong_CT, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addComponent(txtLieuDung_CT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem_CT)
                    .addComponent(btnXoa_CT))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel12, jLabel13, jLabel14, jLabel15, jLabel16, jLabel17});

        jTabbedPane2.addTab("Chi Tiết Đơn Thuốc", jPanel1);

        add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 620));
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void btnHienThiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiActionPerformed
        try {
            loadTableData();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu.", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHienThiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int selectedRow = tblDonThuoc.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn thuốc từ bảng để xóa.", "Chưa chọn", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDon = txtMaDon.getText();
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa đơn thuốc '" + maDon + "' không?",
            "Xác nhận xóa",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                int kq = prescriptionController.delete(maDon);
                if (kq > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    loadTableData(); // Làm mới JTable
                    clearForm();     // Xóa trắng form
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Xóa thất bại. Không tìm thấy mã đơn.", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage() + "\n(Có thể đơn thuốc này đã có chi tiết, bạn phải xóa chi tiết trước.)", "Lỗi CSDL", javax.swing.JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            String maDon = prescriptionController.nextPrescriptionID();
            String maHoSo = (String) comboMaHoSo.getSelectedItem();
            String maBacSi = (String) comboMaBacSi.getSelectedItem();
            java.util.Date ngayKeDon = new java.util.Date();

            if (maHoSo == null || maBacSi == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã Hồ Sơ và Bác Sĩ.", "Thiếu thông tin", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            Prescription p = new Prescription(maHoSo, ngayKeDon, maBacSi);
            p.setMaDon(maDon);

            int kq = prescriptionController.insert(p);

            if (kq > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm đơn thuốc thành công!", "Thành công", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                loadTableData(); // Làm mới JTable
                clearForm();     // Chuẩn bị cho lần nhập tiếp theo

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại. Mã đơn có thể đã tồn tại.", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtNgayKeDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayKeDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKeDonActionPerformed

    private void txtMaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaDonActionPerformed

    private void comboMaBacSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaBacSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboMaBacSiActionPerformed

    private void comboMaHoSoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaHoSoActionPerformed

        if (comboMaHoSo.getSelectedIndex() == -1 || comboMaHoSo.getSelectedItem() == null) {
            return;
        }
        String selectedMaHoSo = (String) comboMaHoSo.getSelectedItem();

        try {
            String maBacSi = prescriptionController.getMaBacSiFromHoSo(selectedMaHoSo);
            if (maBacSi != null) {
                comboMaBacSi.setSelectedItem(maBacSi);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_comboMaHoSoActionPerformed

    private void comboMaDon_CTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaDon_CTActionPerformed
        Object selectedItem = comboMaDon_CT.getSelectedItem();
        
        if (selectedItem != null && comboMaDon_CT.getSelectedIndex() > 0) { // Bỏ qua mục "-- Chọn đơn thuốc --"
            String selectedMaDon = (String) selectedItem;
            // Gọi hàm hỗ trợ để tải JTable
            loadChiTietTable(selectedMaDon);
        } else {
            tableModelChiTiet.setRowCount(0); // Nếu không chọn gì, xóa bảng
        }
    }//GEN-LAST:event_comboMaDon_CTActionPerformed

    private void comboMaThuoc_CTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaThuoc_CTActionPerformed
        Object selectedItem = comboMaThuoc_CT.getSelectedItem();

        // Kiểm tra xem nó có phải là đối tượng Thuoc không (loại bỏ mục "-- Chọn thuốc --")
        if (selectedItem instanceof models.Thuoc) {
            models.Thuoc selectedThuoc = (models.Thuoc) selectedItem;

            // Điền dữ liệu
            txtTenThuoc_CT.setText(selectedThuoc.getTenThuoc());
            txtGia_CT.setText(String.valueOf(selectedThuoc.getGia())); // Giá đơn vị
        } else {
            // Nếu chọn "-- Chọn thuốc --", thì xóa trắng
            txtTenThuoc_CT.setText("");
            txtGia_CT.setText("");
        }
    }//GEN-LAST:event_comboMaThuoc_CTActionPerformed

    private void btnThem_CTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem_CTActionPerformed
        try {
            String maDon = (String) comboMaDon_CT.getSelectedItem();
            Object thuocItem = comboMaThuoc_CT.getSelectedItem();
            String lieuDung = txtLieuDung_CT.getText();
            if (comboMaDon_CT.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một Mã Đơn.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!(thuocItem instanceof models.Thuoc)) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại Thuốc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtSoLuong_CT.getText().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Vui lòng nhập Số Lượng.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            models.Thuoc thuocChon = (models.Thuoc) thuocItem;
            int soLuong = Integer.parseInt(txtSoLuong_CT.getText()); // Chuyển đổi sang số
            models.ChiTietDonThuoc ct = new models.ChiTietDonThuoc();
            ct.setMaDon(maDon);
            ct.setThuoc(thuocChon);
            ct.setSoLuong(soLuong);
            ct.setLieuDung(lieuDung);
            ct.setGia(thuocChon.getGia() * soLuong); // Tính tổng giá = Đơn giá * Số lượng

            // 4. Gọi Controller để INSERT
            int kq = prescriptionController.insertChiTiet(ct);
            
            if (kq > 0) {
                loadChiTietTable(maDon);
                comboMaThuoc_CT.setSelectedIndex(0);
                txtSoLuong_CT.setText("");
                txtLieuDung_CT.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thuốc thất bại (Có thể thuốc này đã có trong đơn).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là một con số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThem_CTActionPerformed

    private void btnXoa_CTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa_CTActionPerformed
        int selectedRow = tblChiTiet.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại thuốc từ bảng chi tiết để xóa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy MaDon và MaThuoc
        String maDon = (String) tableModelChiTiet.getValueAt(selectedRow, 0); // Cột 0 là MaDon
        String maThuoc = (String) tableModelChiTiet.getValueAt(selectedRow, 1); // Cột 1 là MaThuoc
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Xóa thuốc '" + maThuoc + "' khỏi đơn '" + maDon + "'?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int kq = prescriptionController.deleteChiTiet(maDon, maThuoc);
                if (kq > 0) {
                    // Xóa thành công, tải lại JTable
                    loadChiTietTable(maDon);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnXoa_CTActionPerformed

    private void tblChiTietMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblChiTietMouseClicked

    private void tblChiTietMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblChiTietMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHienThi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThem_CT;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoa_CT;
    private javax.swing.JComboBox<String> comboMaBacSi;
    private javax.swing.JComboBox<String> comboMaDon_CT;
    private javax.swing.JComboBox<String> comboMaHoSo;
    private javax.swing.JComboBox<Thuoc> comboMaThuoc_CT;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable tblChiTiet;
    private javax.swing.JTable tblDonThuoc;
    private javax.swing.JTextField txtGia_CT;
    private javax.swing.JTextField txtLieuDung_CT;
    private javax.swing.JTextField txtMaDon;
    private javax.swing.JTextField txtNgayKeDon;
    private javax.swing.JTextField txtSoLuong_CT;
    private javax.swing.JTextField txtTenThuoc_CT;
    // End of variables declaration//GEN-END:variables
}
