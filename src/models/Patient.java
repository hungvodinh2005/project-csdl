package models;

public class Patient {
    private String patientId;
    private String idCard;
    private String guardianId;

    public Patient() {}

    public Patient(String patientId, String idCard, String guardianId) {
        this.patientId = patientId;
        this.idCard = idCard;
        this.guardianId = guardianId;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getGuardianId() { return guardianId; }
    public void setGuardianId(String guardianId) { this.guardianId = guardianId; }
}
