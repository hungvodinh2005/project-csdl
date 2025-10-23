package models;

public class Doctor {
    private String doctorId;
    private String idCard;
    private String specialty;
    private String qualification;

    public Doctor() {}

    public Doctor(String doctorId, String idCard, String specialty, String qualification) {
        this.doctorId = doctorId;
        this.idCard = idCard;
        this.specialty = specialty;
        this.qualification = qualification;
    }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
}
