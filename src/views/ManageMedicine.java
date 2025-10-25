package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Quản lý kho thuốc & đơn thuốc
 * @author PHAMSON
 */
public class ManageMedicine extends JPanel {

    private JTable tblMedicine, tblPrescription;
    private DefaultTableModel modelMedicine, modelPrescription;
    private JTextField txtMedicineId, txtMedicineName, txtUnit, txtPrice;
    private JTextField txtPrescriptionId, txtPatientId, txtDoctorId, txtTotalCost;
    private JComboBox<String> cbMedicine;
    private JSpinner spQuantity;
    private JButton btnAddMedicine, btnUpdateMedicine, btnDeleteMedicine;
    private JButton btnAddPrescription, btnCalcTotal;

    public ManageMedicine() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JTabbedPane tabPane = new JTabbedPane();

        // ========== TAB 1: KHO THUỐC ==========
        JPanel panelMedicine = new JPanel(new BorderLayout());
        JPanel formMedicine = new JPanel(new GridLayout(5, 2, 10, 10));
        formMedicine.setBorder(BorderFactory.createTitledBorder("Thông tin thuốc"));

        txtMedicineId = new JTextField();
        txtMedicineName = new JTextField();
        txtUnit = new JTextField();
        txtPrice = new JTextField();

        formMedicine.add(new JLabel("Mã thuốc:"));
        formMedicine.add(txtMedicineId);
        formMedicine.add(new JLabel("Tên thuốc:"));
        formMedicine.add(txtMedicineName);
        formMedicine.add(new JLabel("Đơn vị tính:"));
        formMedicine.add(txtUnit);
        formMedicine.add(new JLabel("Giá:"));
        formMedicine.add(txtPrice);

        JPanel btnPanel = new JPanel();
        btnAddMedicine = new JButton("Thêm");
        btnUpdateMedicine = new JButton("Sửa");
        btnDeleteMedicine = new JButton("Xóa");
        btnPanel.add(btnAddMedicine);
        btnPanel.add(btnUpdateMedicine);
        btnPanel.add(btnDeleteMedicine);
        formMedicine.add(btnPanel);

        modelMedicine = new DefaultTableModel(new Object[]{"Mã thuốc", "Tên thuốc", "Đơn vị", "Giá"}, 0);
        tblMedicine = new JTable(modelMedicine);
        JScrollPane spMedicine = new JScrollPane(tblMedicine);

        panelMedicine.add(formMedicine, BorderLayout.NORTH);
        panelMedicine.add(spMedicine, BorderLayout.CENTER);

        // ========== TAB 2: ĐƠN THUỐC ==========
        JPanel panelPrescription = new JPanel(new BorderLayout());
        JPanel formPrescription = new JPanel(new GridLayout(5, 2, 10, 10));
        formPrescription.setBorder(BorderFactory.createTitledBorder("Thông tin đơn thuốc"));

        txtPrescriptionId = new JTextField();
        txtPatientId = new JTextField();
        txtDoctorId = new JTextField();
        cbMedicine = new JComboBox<>();
        spQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        txtTotalCost = new JTextField();
        txtTotalCost.setEditable(false);

        formPrescription.add(new JLabel("Mã đơn thuốc:"));
        formPrescription.add(txtPrescriptionId);
        formPrescription.add(new JLabel("Mã bệnh nhân:"));
        formPrescription.add(txtPatientId);
        formPrescription.add(new JLabel("Mã bác sĩ:"));
        formPrescription.add(txtDoctorId);
        formPrescription.add(new JLabel("Chọn thuốc:"));
        formPrescription.add(cbMedicine);
        formPrescription.add(new JLabel("Số lượng:"));
        formPrescription.add(spQuantity);

        JPanel btnPanel2 = new JPanel();
        btnAddPrescription = new JButton("Thêm đơn thuốc");
        btnCalcTotal = new JButton("Tính chi phí");
        btnPanel2.add(btnAddPrescription);
        btnPanel2.add(btnCalcTotal);

        modelPrescription = new DefaultTableModel(
                new Object[]{"Mã đơn", "Mã bệnh nhân", "Mã bác sĩ", "Thuốc", "Số lượng", "Thành tiền"}, 0);
        tblPrescription = new JTable(modelPrescription);
        JScrollPane spPrescription = new JScrollPane(tblPrescription);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Tổng chi phí:"));
        totalPanel.add(txtTotalCost);

        panelPrescription.add(formPrescription, BorderLayout.NORTH);
        panelPrescription.add(spPrescription, BorderLayout.CENTER);
        panelPrescription.add(btnPanel2, BorderLayout.SOUTH);
        panelPrescription.add(totalPanel, BorderLayout.PAGE_END);

        tabPane.addTab("Kho thuốc", panelMedicine);
        tabPane.addTab("Đơn thuốc", panelPrescription);
        add(tabPane, BorderLayout.CENTER);

        // ======= SỰ KIỆN =======
        btnAddMedicine.addActionListener(e -> addMedicine());
        btnAddPrescription.addActionListener(e -> addPrescription());
        btnCalcTotal.addActionListener(e -> calcTotal());
    }

    private void addMedicine() {
        String id = txtMedicineId.getText();
        String name = txtMedicineName.getText();
        String unit = txtUnit.getText();
        double price = Double.parseDouble(txtPrice.getText());
        modelMedicine.addRow(new Object[]{id, name, unit, price});
        cbMedicine.addItem(name);
    }

    private void addPrescription() {
        String presId = txtPrescriptionId.getText();
        String patientId = txtPatientId.getText();
        String doctorId = txtDoctorId.getText();
        String medName = (String) cbMedicine.getSelectedItem();
        int quantity = (int) spQuantity.getValue();

        double price = getPriceByName(medName);
        double total = price * quantity;

        modelPrescription.addRow(new Object[]{presId, patientId, doctorId, medName, quantity, total});
    }

    private double getPriceByName(String name) {
        for (int i = 0; i < modelMedicine.getRowCount(); i++) {
            if (modelMedicine.getValueAt(i, 1).equals(name)) {
                return Double.parseDouble(modelMedicine.getValueAt(i, 3).toString());
            }
        }
        return 0;
    }

    private void calcTotal() {
        double total = 0;
        for (int i = 0; i < modelPrescription.getRowCount(); i++) {
            total += Double.parseDouble(modelPrescription.getValueAt(i, 5).toString());
        }
        txtTotalCost.setText(String.valueOf(total));
    }
}

