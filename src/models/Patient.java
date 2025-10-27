package models;
public class Patient {
    private String patientId;   // MaBenhNhan
    private String idCard;      // CCCD
    private String guardianId;  // NGH_CCCD
    private String medicalHistory; // TienSuBenhAn

    public Patient() {}

    public Patient(String patientId, String idCard, String guardianId, String medicalHistory) {
        this.patientId = patientId;
        this.idCard = idCard;
        this.guardianId = guardianId;
        this.medicalHistory = medicalHistory;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getGuardianId() { return guardianId; }
    public void setGuardianId(String guardianId) { this.guardianId = guardianId; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

     private String hoTen; // Khai báo biến

    public String getHoTen() { // Hàm get
        return hoTen;
    }

    public void setHoTen(String hoTen) { // Hàm set
        this.hoTen = hoTen;
    }
}
}
