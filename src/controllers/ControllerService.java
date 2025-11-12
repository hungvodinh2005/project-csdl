/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connection.JDBCUtil;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class ControllerService {

    public HashMap<String, String> getService() {
        HashMap<String, String> map = new HashMap<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from dichvu";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String ten = rs.getString("tendichvu");
                String ma = rs.getString("madichvu");
                map.put(ten, ma);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ControllerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public static void main(String[] args) {
        ControllerService cs = new ControllerService();
        for(String s : cs.getUsedService("HS012")){
            System.out.println(s);
        }
    }
    
    public ArrayList<String> getUsedService(String maHS){
        ArrayList<String> arr= new ArrayList<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from dichvu d where d.madichvu in (select c.madichvu from chidinhdichvu c where c.mahoso = ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maHS);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String ten = rs.getString("tendichvu");
                arr.add(ten);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
    public HashMap<String, Integer> getUsedServiceAndPrice(String maHS){
        HashMap<String, Integer> arr= new HashMap<>();
        Connection con = JDBCUtil.getConnection();
        String sql = "select * from dichvu d where d.madichvu in (select c.madichvu from chidinhdichvu c where c.mahoso = ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maHS);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String ten = rs.getString("tendichvu");
                int tien = rs.getInt("chiphi");
                arr.put(ten, tien);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
}
