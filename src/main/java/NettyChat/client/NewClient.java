package NettyChat.client;

import NettyChat.client.handler.HttpClientHandler;
import NettyChat.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by ShinChan on 2019/6/5.
 */
public class NewClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "47.111.11.246";
    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception{
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
//                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new HttpResponseDecoder());
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new HttpObjectAggregator(1024*10*1024));
                        ch.pipeline().addLast(new HttpContentDecompressor());
                        ch.pipeline().addLast(new HttpClientHandler());

                    }
                });
        ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
        startConsoleThread(future.channel());
    }
    private static void connect(final Bootstrap bootstrap, final String host, final int port, final int retry) {
        bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
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

    private static void startConsoleThread(Channel channel) throws Exception{
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.println("请输入用户Openid进行登录：");
                    String openid = sc.nextLine();
                    String msg = "";
                    try {
                        msg = "openid="+ URLEncoder.encode(openid, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {
                        URI uri = new URI("/login" + "?" + msg);
                        FullHttpRequest request = new DefaultFullHttpRequest(
                                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString()
                        );
                        request.headers()
                                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                        channel.writeAndFlush(request);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    waitForLoginResponse();
                }else{
                    System.out.println("输入消息发送至服务端: ");
                    String toUserId = sc.next();
                    String message = sc.nextLine();
                    System.out.println(message);
                    try {
                        String msgtmp = "/exit";
                        String msg = "/message?"+"toid="+toUserId.toString()+"&"+"message="+URLEncoder.encode(message, "UTF-8");
                        URI uri = new URI(msg);
                        FullHttpRequest request = new DefaultFullHttpRequest(
                                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString()
                        );
                        request.headers()
                                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                        channel.writeAndFlush(request);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
