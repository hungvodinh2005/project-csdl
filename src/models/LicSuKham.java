/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author CPS
 */
public class LicSuKham {
    private String recordID, patientID, LsID, time, doctorID;

    public LicSuKham(String recordID, String patientID, String LsID, String time, String doctorID) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.LsID = LsID;
        this.time = time;
        this.doctorID = doctorID;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getLsID() {
        return LsID;
    }

    public void setLsID(String LsID) {
        this.LsID = LsID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
    
}
