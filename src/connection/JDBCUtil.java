/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CPS
 */
public class JDBCUtil {
    public static Connection getConnection(){
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/quanlibenhan";
        String user = "root";
        String pass = "";
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("ket noi thanh cong");
        } catch (SQLException ex) {
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}
