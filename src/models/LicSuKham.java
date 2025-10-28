/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author CPS
 */
public class LicSuKham {
    private String recordID, patientID, LsID;
    private LocalDateTime time;
    private String doctorID;
            
    public LicSuKham(String recordID, String patientID, LocalDateTime time, String doctorID) {
        this.recordID = recordID;
        this.patientID = patientID;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
    
}
