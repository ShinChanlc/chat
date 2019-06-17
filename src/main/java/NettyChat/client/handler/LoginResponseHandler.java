package NettyChat.client.handler;

import NettyChat.protocol.OffLineMessage;
import NettyChat.protocol.response.LoginResponsePacket;
import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket){
        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();

        if(loginResponsePacket.isSuccess()){
            SessionUtil.bindSession(new Session(userId), ctx.channel());
            System.out.println("[" + userName + "]登录成功，userId 为: " + userId);
            ArrayList<OffLineMessage> offLineMessages =  loginResponsePacket.getOffLineMessages();
            if (offLineMessages != null){
                for (OffLineMessage offline: offLineMessages
                        ) {
                    System.out.println(offline.getFromId()+ "say: "+ offline.getMessage());
                }
            }
        }else {
            System.out.println("[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

}
