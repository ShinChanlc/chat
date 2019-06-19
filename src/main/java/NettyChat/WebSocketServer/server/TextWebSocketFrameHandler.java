package NettyChat.WebSocketServer.server;

import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息："+msg.text());
        String requestmsg = msg.text();
        String[] array = requestmsg.split("&");
        String openId = array[0];
        try {
            if (!SessionUtil.hasLogin(ctx.channel())){
                SessionUtil.bindSession(new Session(openId), ctx.channel());
                System.out.println("["+ openId +"]登录成功");
            }
        }catch (Exception e){
            System.out.println("登录失败");
            return;
        }
        if (array.length == 3){
            System.out.println("发送信息了啊");
            String sendId = array[0];
            String toId = array[1];
            String message  = array[2];
            MessageManager.sendMessage(sendId, toId, message);
        }
        /**
         * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
         * 传输的就是TextWebSocketFrame类型的数据
         */
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务时间："+ LocalDateTime.now()));
    }

    //每个channel都有一个唯一的id值
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //打印出channel唯一值，asLongText方法是channel的id的全名
        System.out.println("handlerAdded："+ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
        System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.close();
    }
}
