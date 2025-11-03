package models;

import java.sql.Date;

public class Prescription {
    private String prescriptionId;  // MaDon
    private String doctorId;        // MaBacSi
    private String dosage;          // LieuDung
    private Date date;              // NgayKeDon

    public Prescription() {}

    public Prescription(String prescriptionId, String doctorId, String dosage, Date date) {
        this.prescriptionId = prescriptionId;
        this.doctorId = doctorId;
        this.dosage = dosage;
        this.date = date;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId='" + prescriptionId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", dosage='" + dosage + '\'' +
                ", date=" + date +
                '}';
    }
}
