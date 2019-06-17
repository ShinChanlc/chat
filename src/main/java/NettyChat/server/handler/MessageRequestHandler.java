package NettyChat.server.handler;

import NettyChat.mysql.DbConnect;
import NettyChat.protocol.request.LoginRequestPacket;
import NettyChat.protocol.request.MessageRequestPacket;
import NettyChat.protocol.response.MessageResponsePacket;
import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket){
        Session session = SessionUtil.getSession(ctx.channel());
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getOpenId());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
        messageResponsePacket.setToUserId(messageRequestPacket.getToUserId());
        messageResponsePacket.setSendTime(messageRequestPacket.getSendTime());
        System.out.println(new Date()+ "客户端发来的信息："+ messageRequestPacket.getMessage());
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
            toUserChannel.writeAndFlush(messageResponsePacket);
        }else
        {
            try {
                Connection connection = DbConnect.dbConnect.getConnect();
                String sql = "INSERT INTO chat_message (from_id, to_id, message, time) VALUES(?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, messageRequestPacket.getFromUserId());
                ps.setString(2, messageRequestPacket.getToUserId());
                ps.setString(3, messageRequestPacket.getMessage());
                ps.setTimestamp(4, new Timestamp(messageRequestPacket.getSendTime().getTime()));
                ps.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，数据进行存储!");
        }
    }
}
