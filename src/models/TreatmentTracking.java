package Models;

public class TreatmentTracking {
    private String trackingId;
    private String patientId;
    private String doctorId;
    private String note;
    private String reaction;
    private String reExamDate;

    public TreatmentTracking() {}

    public TreatmentTracking(String trackingId, String patientId, String doctorId,
                             String note, String reaction, String reExamDate) {
        this.trackingId = trackingId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.note = note;
        this.reaction = reaction;
        this.reExamDate = reExamDate;
    }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getReaction() { return reaction; }
    public void setReaction(String reaction) { this.reaction = reaction; }

    public String getReExamDate() { return reExamDate; }
    public void setReExamDate(String reExamDate) { this.reExamDate = reExamDate; }
}
