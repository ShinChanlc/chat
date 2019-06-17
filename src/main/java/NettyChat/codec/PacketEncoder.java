package NettyChat.codec;

import NettyChat.protocol.Packet;
import NettyChat.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class PacketEncoder extends MessageToByteEncoder<Packet>{

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out){
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
