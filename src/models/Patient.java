package models;
public class Patient {
   private String patientId;      // mabenhnhan
    private String idCard;         // cccd
    private String guardianId;     // manguoigiamho
    private String maBhyt;         // mabhyt

    public Patient(String patientId, String idCard, String guardianId, String maBhyt) {
        this.patientId = patientId;
        this.idCard = idCard;
        this.guardianId = guardianId;
        this.maBhyt = maBhyt;
    }
    public Patient(){};
    public String getPatientId() {
        return patientId;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public String getMaBhyt() {
        return maBhyt;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }

    public void setMaBhyt(String maBhyt) {
        this.maBhyt = maBhyt;
    }
    
    
}
