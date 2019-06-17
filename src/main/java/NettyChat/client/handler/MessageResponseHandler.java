package NettyChat.client.handler;

import NettyChat.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket){
        String fromUserId = messageResponsePacket.getFromUserId();
        String message = messageResponsePacket.getMessage();
        System.out.println(fromUserId+ " say:" + message);
    }
}
