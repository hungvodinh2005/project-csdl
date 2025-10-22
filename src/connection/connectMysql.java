package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class connectMysql {
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/data?useSSL=false&serverTimezone=UTC";
    private String user="hungvodinh";
    private String password="123456789hH@";
    public  Connection  getConnection() throws SQLException, ClassNotFoundException{
       try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn=DriverManager.getConnection(URL,user,password);
        return conn;
    }catch(SQLException e){
         System.out.println(" Không tìm thấy Driver MySQL!");
            e.printStackTrace();
    }catch(ClassNotFoundException e){
         System.out.println("Lỗi kết nối Database!");
            e.printStackTrace();
    }
    return null;
}
}
