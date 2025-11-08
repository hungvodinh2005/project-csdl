/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import models.MedicalRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class MedicalRecordController {
    public String nextRecordID(){
        String recordID="";
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from hosobenhan order by mahoso desc limit 1";
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            recordID = rs.getString("mahoso");
            int ma = Integer.parseInt(recordID.substring(2));
            recordID = String.format("HS%03d", ma+1);
            con.close();
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recordID;
        
    }
    public int insert(MedicalRecord mr){
        int kq=0;
        Connection con = JDBCUtil.getConnection();
        String sql="INSERT INTO HoSoBenhAn (MaHoSo, MaBenhNhan, NgayNhapVien, ChuanDoan, phuongandieutri, mabacsi) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mr.getRecordId());
            pst.setString(2, mr.getPatientId());
            pst.setString(4, mr.getDiagnosis());
            pst.setDate(3, Date.valueOf(mr.getCreatedDate()));
            pst.setString(5, mr.getPhuongandieutri());
            pst.setString(6, mr.getMabs());
            kq=pst.executeUpdate();
            System.out.println("insert thanh cong");
            con.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(MedicalRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return kq;
    }
    public MedicalRecord selectByMahs(String maHS){
        Connection con = JDBCUtil.getConnection();
        MedicalRecord mr = null;
        String sql = "select * from hosobenhan where mahoso = ? limit 1";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maHS);
            ResultSet rs = pst.executeQuery();
            rs.next();
            String maBN = rs.getString("mabenhnhan");
            LocalDate ngayvao = rs.getDate("ngaynhapvien").toLocalDate();
            LocalDate ngayra = rs.getDate("ngayxuatvien").toLocalDate();
            String chuandoan = rs.getString("chuandoan");
            String dieutri = rs.getString("phuongandieutri");
            String ketqua = rs.getString("ketqua");
            String mabs = rs.getString("mabacsi");
            mr=new MedicalRecord(maHS, maBN,mabs, chuandoan, ngayvao, ngayra, ketqua, dieutri);
            con.close();
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(MedicalRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mr;
    }
    public static void main(String[] args) {
        MedicalRecordController mrc = new MedicalRecordController();
        System.out.println(mrc.nextRecordID());
    }
}
