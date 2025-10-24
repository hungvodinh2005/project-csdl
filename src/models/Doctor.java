package models;
public class Doctor {
    private String doctorId;     // MaBacSi
    private String idCard;       // CCCD (liên kết Person)
    private String specialty;    // ChuyenKhoa

    public Doctor() {}

    public Doctor(String doctorId, String idCard, String specialty) {
        this.doctorId = doctorId;
        this.idCard = idCard;
        this.specialty = specialty;
    }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
