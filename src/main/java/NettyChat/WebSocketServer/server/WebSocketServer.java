package NettyChat.WebSocketServer.server;

/**
 * Created by ShinChan on 2019/6/19.
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class WebSocketServer {
    private static final int PORT = 12000;

    public static void main(String[] args) throws Exception{
        NioEventLoopGroup BoosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(BoosGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketChannelInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(PORT)).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            BoosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}