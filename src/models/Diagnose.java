package models;

public class Diagnose{
    private String diagnosisId;
    private String scheduleId;
    private String doctorId;
    private String symptom;
    private String result;
    private String serviceId;

    public Diagnose() {}

    public Diagnose(String diagnosisId, String scheduleId, String doctorId, String symptom, String result, String serviceId) {
        this.diagnosisId = diagnosisId;
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.symptom = symptom;
        this.result = result;
        this.serviceId = serviceId;
    }

    public String getDiagnosisId() { return diagnosisId; }
    public void setDiagnosisId(String diagnosisId) { this.diagnosisId = diagnosisId; }

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
}
