//package NettyChat.server;
//
//import NettyChat.protocol.Packet;
//import NettyChat.protocol.PacketCodeC;
//import NettyChat.protocol.request.LoginRequestPacket;
//import NettyChat.protocol.request.MessageRequestPacket;
//import NettyChat.protocol.response.LoginResponsePacket;
//import NettyChat.protocol.response.MessageResponsePacket;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//import java.nio.charset.Charset;
//import java.util.Date;
//
///**
// * Created by ShinChan on 2019/5/28.
// */
//public class FirstServerHandler extends ChannelInboundHandlerAdapter {
////    @Override
////    public void channelActive(ChannelHandlerContext ctx){
////        ByteBuf msg  = getHelloBuf(ctx);
////        ctx.channel().writeAndFlush(msg);
////    }
////
////    private ByteBuf getHelloBuf(ChannelHandlerContext ctx){
////        byte[] bytes = "welcome to join our server!".getBytes(Charset.forName("utf-8"));
////        ByteBuf buf = ctx.alloc().buffer();
////        buf.writeBytes(bytes);
////        return buf;
////    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
//        if (packet instanceof LoginRequestPacket){
//            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
//            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//            loginResponsePacket.setVersion(packet.getVersion());
//            if (valid(loginRequestPacket)){
//                //验证成功
//                System.out.println(new Date() + ": 登陆成功");
//                loginResponsePacket.setSuccess(true);
//            }else {
//                //验证失败
//                loginResponsePacket.setSuccess(false);
//                loginResponsePacket.setReason("账号密码验证失败");
//                System.out.println(new Date() + "登录失败！");
//            }
//            // 回复数据到客户端
//            ByteBuf responceBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
//            ctx.channel().writeAndFlush(responceBuf);
//        }else if(packet instanceof MessageRequestPacket){
//            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
//            System.out.println(new Date()+ "收到了客户端消息："+ messageRequestPacket.getMessage());
//            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//            messageResponsePacket.setMessage("服务端回复【"+ messageRequestPacket.getMessage()+"】");
//            ByteBuf responseBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//            ctx.channel().writeAndFlush(responseBuf);
//
//        }else{
//            System.out.println("错误的包");
//        }
//    }
//
//    private boolean valid(LoginRequestPacket loginRequestPacket){
//        return true;
//    }
//
//    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
//        byte[] bytes = "你好，客户端!".getBytes(Charset.forName("utf-8"));
//
//        ByteBuf buffer = ctx.alloc().buffer();
//
//        buffer.writeBytes(bytes);
//
//        return buffer;
//    }
//}
