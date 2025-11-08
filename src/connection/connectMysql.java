package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectMysql {
<<<<<<< HEAD
    private static final String URL = 
        "jdbc:mysql://localhost:3306/csdl?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Son0945@";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Không tìm thấy Driver MySQL!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối Database!");
            e.printStackTrace();
        }
=======
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/csdl?useSSL=false&serverTimezone=UTC";
    private String user="root";
    private String password="";
    public  Connection  getConnection() throws SQLException, ClassNotFoundException{
       try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn=DriverManager.getConnection(URL,user,password);
>>>>>>> da8df0f42885cbec4746a1687e1a0d991465e400
        return conn;
    }
}
