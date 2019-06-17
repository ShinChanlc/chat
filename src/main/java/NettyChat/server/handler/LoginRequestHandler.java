package NettyChat.server.handler;

import NettyChat.mysql.DbConnect;
import NettyChat.protocol.OffLineMessage;
import NettyChat.protocol.request.LoginRequestPacket;
import NettyChat.protocol.response.LoginResponsePacket;
import NettyChat.protocol.response.OffLineMessagePacket;
import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        //登录逻辑
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        if(valid(loginRequestPacket)){
            loginResponsePacket.setSuccess(true);
            String userId = loginRequestPacket.getUserId();
            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setUserName(loginRequestPacket.getUserName());
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(new Session(userId), ctx.channel());
//            String sql = "select * from tb_user";
            String sql = "SELECT * FROM chat_message WHERE to_id = " + userId;
            Connection connection = DbConnect.dbConnect.getConnect();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                OffLineMessage offLineMessage = new OffLineMessage();
                offLineMessage.setFromId(resultSet.getString("from_id"));
                offLineMessage.setToId((resultSet.getString("to_id")));
                offLineMessage.setMessage(resultSet.getString("message"));
                offLineMessage.setSendTime(resultSet.getTimestamp("time"));
                System.out.println(offLineMessage.toString());
                System.out.println(loginResponsePacket.getUserName());
                loginResponsePacket.addOffLineMessage(offLineMessage);
            }
            String deleteSql = "DELETE FROM chat_message WHERE to_id = " + userId;
            PreparedStatement ps1 = connection.prepareStatement(deleteSql);
            ps1.execute();
        }else {
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("账号密码验证失败");
            System.out.println(new Date() + "登录失败!");
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
