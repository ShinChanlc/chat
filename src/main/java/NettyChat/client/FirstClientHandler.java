//package NettyChat.client;
//
//import NettyChat.protocol.Packet;
//import NettyChat.protocol.PacketCodeC;
//import NettyChat.protocol.request.LoginRequestPacket;
//import NettyChat.protocol.response.LoginResponsePacket;
//import NettyChat.protocol.response.MessageResponsePacket;
//import NettyChat.util.LoginUtil;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//import java.nio.charset.Charset;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * Created by ShinChan on 2019/5/28.
// */
//public class FirstClientHandler extends ChannelInboundHandlerAdapter {
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ": 客户端开始登录");
//
//        // 1.创建登录对象
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(UUID.randomUUID().toString());
//        loginRequestPacket.setUsername("shinchan");
//        loginRequestPacket.setPassword("zlc");
//        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);
//        // 2.写数据
//        ctx.channel().writeAndFlush(buffer);
//    }
//
//    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
//        byte[] bytes = "你好，服务器!".getBytes(Charset.forName("utf-8"));
//
//        ByteBuf buffer = ctx.alloc().buffer();
//
//        buffer.writeBytes(bytes);
//
//        return buffer;
//    }
//
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
//        if (packet instanceof LoginResponsePacket){
//            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
//            if (loginResponsePacket.isSuccess()){
//                LoginUtil.markAsLogin(ctx.channel());
//                System.out.println(new Date() + "客户端登录成功");
//            }else {
//                System.out.println(new Date() + "客户端登录失败，原因是："+ loginResponsePacket.getReason());
//            }
//        }else if(packet instanceof MessageResponsePacket){
//            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
//            System.out.println(new Date()+"服务端回复信息："+ messageResponsePacket.getMessage());
//        }
//        else {
//            System.out.println("packet is not response");
//        }
//    }
//}
