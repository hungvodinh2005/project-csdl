package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageUpdateStatus extends JPanel {

    private JTable tblTreatment;
    private DefaultTableModel modelTreatment;
    private JTextField txtTrackingId, txtPatientId, txtDoctorId, txtStatus;
    private JTextArea txtNote;
    private JButton btnAdd, btnUpdate, btnDelete, btnRemind;

    public ManageUpdateStatus() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ====== TIÊU ĐỀ ======
        JLabel lblTitle = new JLabel("Theo dõi điều trị", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // ====== FORM ======
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin theo dõi"));

        txtTrackingId = new JTextField();
        txtPatientId = new JTextField();
        txtDoctorId = new JTextField();
        txtStatus = new JTextField();
        txtNote = new JTextArea(3, 20);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);

        formPanel.add(new JLabel("Mã theo dõi:"));
        formPanel.add(txtTrackingId);
        formPanel.add(new JLabel("Mã bệnh nhân:"));
        formPanel.add(txtPatientId);
        formPanel.add(new JLabel("Mã bác sĩ:"));
        formPanel.add(txtDoctorId);
        formPanel.add(new JLabel("Tình trạng bệnh:"));
        formPanel.add(txtStatus);
        formPanel.add(new JLabel("Ghi chú / Phản ứng thuốc:"));
        formPanel.add(new JScrollPane(txtNote));

        add(formPanel, BorderLayout.WEST);

        // ====== BẢNG DỮ LIỆU ======
        String[] columns = {"Mã theo dõi", "Mã BN", "Mã BS", "Tình trạng", "Ghi chú"};
        modelTreatment = new DefaultTableModel(columns, 0);
        tblTreatment = new JTable(modelTreatment);
        JScrollPane scrollPane = new JScrollPane(tblTreatment);
        add(scrollPane, BorderLayout.CENTER);

        // ====== NÚT CHỨC NĂNG ======
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnRemind = new JButton("Gửi nhắc tái khám");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRemind);
        add(btnPanel, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
        btnAdd.addActionListener(e -> addTracking());
        btnUpdate.addActionListener(e -> updateTracking());
        btnDelete.addActionListener(e -> deleteTracking());
        btnRemind.addActionListener(e -> remindPatient());
    }

    private void addTracking() {
        String id = txtTrackingId.getText();
        String patient = txtPatientId.getText();
        String doctor = txtDoctorId.getText();
        String status = txtStatus.getText();
        String note = txtNote.getText();

        if (id.isEmpty() || patient.isEmpty() || doctor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        modelTreatment.addRow(new Object[]{id, patient, doctor, status, note});
        clearForm();
    }

    private void updateTracking() {
        int selected = tblTreatment.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để cập nhật!");
            return;
        }

        modelTreatment.setValueAt(txtTrackingId.getText(), selected, 0);
        modelTreatment.setValueAt(txtPatientId.getText(), selected, 1);
        modelTreatment.setValueAt(txtDoctorId.getText(), selected, 2);
        modelTreatment.setValueAt(txtStatus.getText(), selected, 3);
        modelTreatment.setValueAt(txtNote.getText(), selected, 4);

        JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin!");
    }

    private void deleteTracking() {
        int selected = tblTreatment.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xóa!");
            return;
        }

        modelTreatment.removeRow(selected);
        JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
    }

    private void remindPatient() {
        JOptionPane.showMessageDialog(this, "Đã gửi nhắc tái khám cho bệnh nhân qua hệ thống!");
    }

    private void clearForm() {
        txtTrackingId.setText("");
        txtPatientId.setText("");
        txtDoctorId.setText("");
        txtStatus.setText("");
        txtNote.setText("");
    }
    public static void main(String[] args) {
        // Chạy trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Tạo một cửa sổ (JFrame)
            JFrame frame = new JFrame("Test Giao Diện Theo Dõi Điều Trị");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 2. Tạo một đối tượng của Panel
            ManageUpdateStatus mainPanel = new ManageUpdateStatus();

            // 3. Thêm panel vào làm nội dung chính của cửa sổ
            frame.setContentPane(mainPanel);

            // 4. Tự động điều chỉnh kích thước
            frame.pack();

            // 5. Đặt ra giữa màn hình
            frame.setLocationRelativeTo(null);

            // 6. Hiển thị
            frame.setVisible(true);
        });
    }
}
