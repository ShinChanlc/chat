package NettyChat.client.handler;

import NettyChat.serialize.Serializer;
import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

/**
 * Created by ShinChan on 2019/6/5.
 */
public class HttpClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if (msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf content = response.content();
            String res = content.toString(CharsetUtil.UTF_8);
            JSONObject jsonObjectRes = JSONObject.parseObject(res);
            if (jsonObjectRes.getString("type").equals("login")){
                SessionUtil.bindSession(new Session(jsonObjectRes.getString("openid")), ctx.channel());
                System.out.println("登录响应");
                System.out.println(res);
            }
            else if (jsonObjectRes.getString("type").equals("message")){
                System.out.println("实时收信息");
                System.out.println(res);
            }

        }
    }
}
