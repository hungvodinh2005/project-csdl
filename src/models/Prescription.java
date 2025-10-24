package models;
public class Prescription {
    private String prescriptionId;  // MaDon
    private String doctorId;        // MaBacSi
    private String dosage;          // LieuDung
    private java.sql.Date date;     // NgayKeDon

    public Prescription() {}

    public Prescription(String prescriptionId, String doctorId, String dosage, java.sql.Date date) {
        this.prescriptionId = prescriptionId;
        this.doctorId = doctorId;
        this.dosage = dosage;
        this.date = date;
    }

    // Getters & Setters
}
