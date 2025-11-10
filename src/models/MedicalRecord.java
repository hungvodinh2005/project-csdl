package models;

import java.time.LocalDate;
import java.util.Date;

public class MedicalRecord {
    private String recordId;
    private String patientId, mabs;
    private String diagnosis;
    private LocalDate createdDate, ngayxuat;
    private String status, phuongandieutri;

    public MedicalRecord() {}



    public MedicalRecord(String recordId, String patientId, String mabs, String diagnosis, LocalDate createdDate, String phuongandieutri) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.mabs = mabs;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
        this.phuongandieutri = phuongandieutri;
    }

    public MedicalRecord(String recordId, String patientId, String mabs, String diagnosis, LocalDate createdDate, LocalDate ngayxuat, String status, String phuongandieutri) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.mabs = mabs;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
        this.ngayxuat = ngayxuat;
        this.status = status;
        this.phuongandieutri = phuongandieutri;
    }
    


    
    public LocalDate getNgayxuat() {
        return ngayxuat;
    }

    public void setNgayxuat(LocalDate ngayxuat) {
        this.ngayxuat = ngayxuat;
    }

    public String getMabs() {
        return mabs;
    }

    public void setMabs(String mabs) {
        this.mabs = mabs;
    }

    public String getPhuongandieutri() {
        return phuongandieutri;
    }

    public void setPhuongandieutri(String phuongandieutri) {
        this.phuongandieutri = phuongandieutri;
    }

    public MedicalRecord(String recordId, String patientId, String diagnosis, LocalDate createdDate) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
    }
    
    
    

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
