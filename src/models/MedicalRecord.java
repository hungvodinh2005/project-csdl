package models;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String diagnosis;
    private String createdDate, ngayxuat;
    private String status, phuongandieutri;

    public MedicalRecord() {}

    public MedicalRecord(String recordId, String patientId, String diagnosis, String createdDate, String status) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
        this.status = status;
    }
    

    public MedicalRecord(String recordId, String patientId, String diagnosis, String createdDate, String ngayxuat, String status, String pa) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
        this.status = status;
        this.ngayxuat = ngayxuat;
        this.phuongandieutri = pa;
    }

    public String getNgayxuat() {
        return ngayxuat;
    }

    public void setNgayxuat(String ngayxuat) {
        this.ngayxuat = ngayxuat;
    }

    public String getPhuongandieutri() {
        return phuongandieutri;
    }

    public void setPhuongandieutri(String phuongandieutri) {
        this.phuongandieutri = phuongandieutri;
    }

    public MedicalRecord(String recordId, String patientId, String diagnosis, String createdDate) {
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

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
