package NettyChat.Test;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShinChan on 2019/6/3.
 */
public class Test {
    public static void main(String[] args){
        Connection connection = getConnection();
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = dFormat.format(new Date());
        String sql = "INSERT INTO chat_message (from_id, to_id, message, time) VALUES(?,?,?,?)";
        PreparedStatement ps;
        int i= 0;
        try {
            ps = (PreparedStatement) connection.prepareStatement(sql);
            ps.setString(1, "110");
            ps.setString(2, "130");
            ps.setString(3, "HELLO, I AM COMING!");
            Timestamp dates = Timestamp.valueOf(sendTime);
            ps.setTimestamp(4, dates);
            i = ps.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        sql = "SELECT * FROM chat_message WHERE to_id = " + "130";
        try {
            ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getTimestamp("time"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static Connection getConnection(){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.105.242.49:3306/face_mysql?characterEncoding=utf-8";
        String userName = "shinchan";
        String password = "shinchan";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return conn;
    }
}
