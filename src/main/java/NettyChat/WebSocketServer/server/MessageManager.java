package NettyChat.WebSocketServer.server;

import NettyChat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by ShinChan on 2019/6/19.
 */
public class MessageManager {

    public static void sendMessage(String sendId, String toId, String message){
        Channel toIDChannel = SessionUtil.getChannel(toId);
        if (toIDChannel != null && SessionUtil.hasLogin(toIDChannel)){
            toIDChannel.writeAndFlush(new TextWebSocketFrame(message));
        }else {
            //离线存储
            System.out.println("对方不在线");
        }
    }
}
