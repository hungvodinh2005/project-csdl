package models;
public class MedicalRecord {
    private String recordId;         // MaHoSo
    private String patientId;        // MaBenhNhan
    private java.sql.Date admissionDate; // NgayNhapVien
    private java.sql.Date dischargeDate; // NgayXuatVien
    private String diagnosis;        // ChanDoan
    private String treatmentPlan;    // PhuongAnDieuTri
    private String result;           // KetQua

    public MedicalRecord() {}

    public MedicalRecord(String recordId, String patientId, java.sql.Date admissionDate,
                         java.sql.Date dischargeDate, String diagnosis,
                         String treatmentPlan, String result) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.result = result;
    }

    // Getters & Setters
    // ...
}
