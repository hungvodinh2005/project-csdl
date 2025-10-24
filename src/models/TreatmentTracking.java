package models;
public class TreatmentTracking {
    private String doctorId;
    private String recordId;

    public TreatmentTracking() {}

    public TreatmentTracking(String doctorId, String recordId) {
        this.doctorId = doctorId;
        this.recordId = recordId;
    }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
}
