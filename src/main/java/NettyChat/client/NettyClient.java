package NettyChat.client;

//import NettyChat.client.FirstClientHandler;
import NettyChat.client.handler.LoginResponseHandler;
import NettyChat.client.handler.MessageResponseHandler;
import NettyChat.codec.PacketDecoder;
import NettyChat.codec.PacketEncoder;
import NettyChat.codec.Spliter;
import NettyChat.protocol.Packet;
import NettyChat.protocol.PacketCodeC;
import NettyChat.protocol.request.LoginRequestPacket;
import NettyChat.protocol.request.MessageRequestPacket;
import NettyChat.server.handler.AuthHandler;
import NettyChat.server.handler.MessageRequestHandler;
import NettyChat.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by ShinChan on 2019/5/28.
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args){
        NioEventLoopGroup group = new NioEventLoopGroup();

        final Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch){
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }
    private static void connect(final Bootstrap bootstrap, final String host, final int port, final int retry) {
        bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("连接成功!启动控制台......");
                    Channel channel = ((ChannelFuture) future).channel();
                    startConsoleThread(channel);
                } else if (retry == 0) {
                    System.err.println("重试次数已用完，放弃连接！");
                } else {
                    // 第几次重连
                    int order = (MAX_RETRY - retry) + 1;
                    // 本次重连的间隔
                    int delay = 1 << order;
                    System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                    connect(bootstrap, host, port+1, retry-1);
                }
            }
        });
    }

    private static void startConsoleThread(Channel channel){
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.println("请输入用户id和用户名进行登录(userID userName)：");
                    loginRequestPacket.setUserId(sc.next());
                    loginRequestPacket.setUserName(sc.next());
                    loginRequestPacket.setPassword("default");
                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
                }else{
                    System.out.println("输入消息发送至服务端: ");
                    String toUserId = sc.next();
                    String message = sc.nextLine();
                    String fromUserId = SessionUtil.getSession(channel).getOpenId();
//                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date sendTime = new Date();
                    MessageRequestPacket packet = new MessageRequestPacket(fromUserId, toUserId, message, sendTime);
                    channel.writeAndFlush(packet);
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
