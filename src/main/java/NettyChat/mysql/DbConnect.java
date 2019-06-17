package NettyChat.mysql;

/**
 * Created by ShinChan on 2019/6/2.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.alibaba.druid.pool.DruidDataSource;
public class DbConnect {

    public static DbConnect dbConnect;
    static {
        try {
            dbConnect = new DbConnect();
            System.out.println("我执行了一次哟");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DruidDataSource dataSource = null;
    public DbConnect() throws Exception {
        GetDbConnect();
    }

    public void GetDbConnect() throws Exception{
        try {
            if (dataSource == null){
                dataSource = new DruidDataSource();
                dataSource.setUrl("jdbc:mysql://10.105.242.49:3306/face_mysql?characterEncoding=utf-8");
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUsername("shinchan");
                dataSource.setPassword("shinchan");
                dataSource.setInitialSize(1);
                dataSource.setMinIdle(1);
                dataSource.setMaxActive(20);
                //连接泄漏监测
                dataSource.setRemoveAbandoned(true);
                dataSource.setRemoveAbandonedTimeout(30);
                //配置获取连接等待超时的时间
                dataSource.setMaxWait(20000);
                //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                dataSource.setTimeBetweenEvictionRunsMillis(20000);
                //防止过期
                dataSource.setValidationQuery("SELECT 'x'");
                dataSource.setTestWhileIdle(true);
                dataSource.setTestOnBorrow(true);
            }
        }catch (Exception e){
            throw e;
        }
    }

    public Connection getConnect() throws Exception{
        Connection con = null;
        try {
            con = dataSource.getConnection();
        }catch (Exception e){
            throw e;
        }
        return con;
    }

    public static void main(String[] agrs) throws Exception {
        DbConnect dbConnect = new DbConnect();
        try {
            Connection connection = dbConnect.getConnect();
            String sql = "select * from tb_user";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }
//            if (resultSet.next()){
//                System.out.println(resultSet.getString("username"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
