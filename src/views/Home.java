package views;

import Models.User;
import java.awt.*;
import javax.swing.*;

public class Home {
    private JFrame frame;

    public Home(User user) {
        frame = new JFrame("Trang chủ quản lý bệnh viện");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        
        CardLayout cardLayout = new CardLayout();
        JPanel center = new JPanel(cardLayout);
        center.setBackground(new Color(220, 220, 220));
        
        
        JPanel header = new JPanel();
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ Y TẾ");
        title.setForeground(Color.WHITE);       
        title.setFont(new Font("Arial", Font.BOLD, 24)); 
        header.setBackground(new Color(64, 64, 64));
        header.setPreferredSize(new Dimension(screenSize.width, 80));
        header.add(title);
        
       
        JPanel sider = new JPanel(new GridLayout(12, 1, 10, 15));
        sider.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        sider.setBackground(Color.LIGHT_GRAY);
        sider.setPreferredSize(new Dimension(220, screenSize.height));
        
        // Các nút điều hướng
        JButton btnPatients = new JButton("Bệnh nhân");
        JButton btnPerson = new JButton("Thông tin cá nhân");
        JButton btnDoctors = new JButton("Bác sĩ");
        JButton btnSchedule = new JButton("Lịch khám");
        JButton btnDiagnose = new JButton("Khám bệnh");
        JButton btnRecords = new JButton("Hồ sơ bệnh án");
        JButton btnTracking = new JButton("Theo dõi điều trị");
        JButton btnMedicine = new JButton("Kho thuốc");
        JButton btnService = new JButton("Dịch vụ y tế");
        JButton btnReport = new JButton("Báo cáo");
        JButton btnLogout = new JButton("Đăng xuất");
        
        JButton[] buttons = {btnPatients,btnPerson, btnDoctors, btnSchedule, btnDiagnose, btnRecords,
                             btnTracking, btnMedicine, btnService, btnReport, btnLogout};
        
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setBackground( new Color(255, 140, 0));
            b.setForeground(Color.WHITE);
            sider.add(b);
        }

        // Thêm panel con (chỉ cần có class đó)
        
        center.add(new ManagePerson(), "Person");
        center.add(new ManageDoctor(), "Doctor");
        center.add(new ManageSchedule(), "Schedule");
        center.add(new ManageDiagnose(), "Diagnose");
        center.add(new ManageRecords(), "Records");
//        center.add(new ManageUpdateStatus(), "Tracking");
        center.add(new ManageService(), "Service");
        center.add(new Report(), "Report");
        center.add(new ManagePrescription(), "Prescription");

        // Sự kiện chuyển trang
        btnPatients.addActionListener(e -> cardLayout.show(center, "Patients"));
        btnPerson.addActionListener(e -> cardLayout.show(center, "Person"));
        btnDoctors.addActionListener(e -> cardLayout.show(center, "Doctor"));
        btnSchedule.addActionListener(e -> cardLayout.show(center, "Schedule"));
        btnDiagnose.addActionListener(e -> cardLayout.show(center, "Diagnose"));
        btnRecords.addActionListener(e -> cardLayout.show(center, "Records"));
        btnTracking.addActionListener(e -> cardLayout.show(center, "Tracking"));
        btnMedicine.addActionListener(e -> cardLayout.show(center, "Prescription"));
        btnService.addActionListener(e -> cardLayout.show(center, "Service"));
        btnReport.addActionListener(e -> cardLayout.show(center, "Report"));
        
        

        frame.add(header, BorderLayout.NORTH);
        frame.add(sider, BorderLayout.WEST);
        frame.add(center, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

  public void showHome() { frame.setVisible(true); }
  public JFrame getFrame() { return frame; }
}
