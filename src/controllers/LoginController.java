/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import Connect.connectMysql;
import Models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    public boolean check(String user,String password){
        String sql="SELECT * FROM users WHERE username = ? AND password = ?";
        try{
            Connection conn=new connectMysql().getConnection();
            PreparedStatement test=conn.prepareStatement(sql);
            test.setString(1, user);
            test.setString(2, password);
            ResultSet rs = test.executeQuery();
            
            return rs.next();
            
        }catch(Exception e){
            System.out.println("error");
        }
        return true;
    }
    public User objectUser(String user,String password){
        String sql="SELECT * FROM users WHERE username = ? AND password = ?";
        User users=new User();
        try{
            Connection conn=new connectMysql().getConnection();
            PreparedStatement test=conn.prepareStatement(sql);
            test.setString(1, user);
            test.setString(2, password);
            ResultSet rs = test.executeQuery();
            while(rs.next()){
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setPassword(rs.getString("password"));
            }
            
            
            
        }catch(Exception e){
            System.out.println("error");
        }
        return users;
    }
    
}
