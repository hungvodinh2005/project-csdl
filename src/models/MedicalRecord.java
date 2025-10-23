package models;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String diagnosis;
    private String createdDate;
    private String status;

    public MedicalRecord() {}

    public MedicalRecord(String recordId, String patientId, String diagnosis, String createdDate, String status) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.createdDate = createdDate;
        this.status = status;
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
