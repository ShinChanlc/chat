package NettyChat.server.handler;

import NettyChat.mysql.DbConnect;
import NettyChat.serialize.Serializer;
import NettyChat.session.Session;
import NettyChat.util.SessionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
/**
 * Created by ShinChan on 2019/6/4.
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter{

    private static final String FAVICON_ICON = "/favicon.ico";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.getUri());
            String path = uri.getPath();
//            String url = request.uri();
            if (path.equals(FAVICON_ICON))
                return;
            System.out.println(request.uri());
            Map<String, String> parmMap = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().entrySet().forEach(entry ->{
                parmMap.put(entry.getKey(), entry.getValue().get(0));
            });
            String requestType = path.substring(1);
            if (requestType.equals("login")){
                //处理用户登录的逻辑
                String openId = parmMap.get("openid");
                System.out.println(openId);
                try {
                    SessionUtil.bindSession(new Session(openId), ctx.channel());
                    System.out.println("["+ openId +"]登录成功");
                }catch (Exception e){
                    System.out.println("登录失败");
                    return;
                }
                String sql = "SELECT * FROM chat WHERE to_id = '" + openId + "'";
                System.out.println(sql);
                Connection connection = DbConnect.dbConnect.getConnect();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery();
                Map<String, JSONObject> messageMap = new HashMap<>();
                while (resultSet.next()){
                    String fromId = resultSet.getString("from_id");
                    JSONObject messageObject = new JSONObject();
                    if (messageMap.containsKey(fromId)){
                        Timestamp timestamp = resultSet.getTimestamp("time");
                        if (timestamp.getTime() > messageMap.get(fromId).getTimestamp("time").getTime()){
                            messageMap.get(fromId).put("time", timestamp);
                            messageMap.get(fromId).put("message", resultSet.getString("message"));
                        }
                    }else {
                        messageObject.put("fromid", fromId);
                        messageObject.put("username", "shinchan");
                        messageObject.put("message", resultSet.getString("message"));
                        messageObject.put("time", resultSet.getTimestamp("time"));
                        messageMap.put(fromId, messageObject);
                    }
                }
                Set<String> set = messageMap.keySet();
                Iterator iterator = set.iterator();
                JSONArray jsonArrayRes = new JSONArray();
                while (iterator.hasNext()){
                    String fromId = (String) iterator.next();
                    jsonArrayRes.add(messageMap.get(fromId));
                }
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("error_code",0);
                jsonObjectRes.put("type", "login");
                jsonObjectRes.put("openid", openId);
                jsonObjectRes.put("data", jsonArrayRes);
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(Serializer.DEFAULT.serialize(jsonObjectRes))
                );
                response.headers().set("Content-Type", "text/html; charset=UTF-8");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);

            }else if (requestType.equals("exit")){
                //处理用户退出的逻辑
                SessionUtil.unBindSession(ctx.channel());
                ctx.channel().close();
                ctx.close();
                System.out.println("关闭channel!");
            }else if (requestType.equals("wchat")){
                String fromOpenId = parmMap.get("fromid");
                String toOpenId = parmMap.get("toid");
                try {
                    SessionUtil.bindSession(new Session(fromOpenId), ctx.channel());
                    System.out.println("["+ fromOpenId +"]登录成功");
                }catch (Exception e){
                    System.out.println("登录失败");
                    return;
                }
                JSONObject jsonres = new JSONObject();
                try {
                    String sql = "SELECT * FROM chat WHERE to_id = '" + toOpenId + "' AND from_id = '" + fromOpenId + "'";
                    System.out.println(sql);
                    Connection connection = DbConnect.dbConnect.getConnect();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ResultSet resultSet = ps.executeQuery();
                    JSONArray jsondata = new JSONArray();
                    while (resultSet.next()){
                        JSONObject messageObject = new JSONObject();
                        messageObject.put("message", resultSet.getString("message"));
                        messageObject.put("time", resultSet.getString("time"));
                        jsondata.add(messageObject);
                    }
                    jsonres.put("error_code", 0);
                    jsonres.put("to_id", toOpenId);
                    jsonres.put("username", "shinchan");
                    jsonres.put("data", jsondata);
                    String deleteSql = "DELETE FROM chat WHERE to_id = '" + toOpenId + "' AND from_id = '" +fromOpenId + "'";
                    PreparedStatement ps1 = connection.prepareStatement(deleteSql);
                    ps1.execute();
                }catch (Exception e){
                    e.printStackTrace();
                    jsonres.put("error_code", 1);
                }
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(Serializer.DEFAULT.serialize(jsonres))
                );
                response.headers().set("Content-Type", "text/html; charset=UTF-8");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }else if (requestType.equals("userchat")){
                String fromOpenId = parmMap.get("fromid");
                String toOpenId = parmMap.get("toid");
                JSONObject jsonres = new JSONObject();
                try {
                    String sql = "SELECT * FROM chat WHERE to_id = '" + toOpenId + "' AND from_id = '" + fromOpenId + "'";
                    System.out.println(sql);
                    Connection connection = DbConnect.dbConnect.getConnect();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ResultSet resultSet = ps.executeQuery();
                    JSONArray jsondata = new JSONArray();
                    while (resultSet.next()){
                        JSONObject messageObject = new JSONObject();
                        messageObject.put("message", resultSet.getString("message"));
                        messageObject.put("time", resultSet.getString("time"));
                        jsondata.add(messageObject);
                    }
                    jsonres.put("error_code", 0);
                    jsonres.put("to_id", toOpenId);
                    jsonres.put("username", "shinchan");
                    jsonres.put("data", jsondata);
                    String deleteSql = "DELETE FROM chat WHERE to_id = '" + toOpenId + "' AND from_id = '" +fromOpenId + "'";
                    PreparedStatement ps1 = connection.prepareStatement(deleteSql);
                    ps1.execute();
                }catch (Exception e){
                    e.printStackTrace();
                    jsonres.put("error_code", 1);
                }
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(Serializer.DEFAULT.serialize(jsonres))
                );
                response.headers().set("Content-Type", "text/html; charset=UTF-8");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
            //客户端发来聊天信息，进行处理 分别考虑接收方在线和不在线两种情况
            else if(requestType.equals("message")){
                String fromOpenId = SessionUtil.getSession(ctx.channel()).getOpenId();
                String toOpenId = parmMap.get("toid");
                System.out.println(toOpenId);
                String message = parmMap.get("message");
                Timestamp timestamp = new Timestamp(new Date().getTime());
                Channel toUserChannel = SessionUtil.getChannel(toOpenId);
                if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
                    JSONObject MessageJsonObject = new JSONObject();
                    MessageJsonObject.put("type", "message");
                    MessageJsonObject.put("fromid", fromOpenId);
                    MessageJsonObject.put("message", message);
                    MessageJsonObject.put("time", timestamp);
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(
                                    Serializer.DEFAULT.serialize(MessageJsonObject))
                    );
                    response.headers().set("Content-Type", "text/html; charset=UTF-8");
                    response.headers().set(CONTENT_LENGTH,
                            response.content().readableBytes());
                    response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    toUserChannel.writeAndFlush(response);
                }else {
                    //离线存储
                    try {
                        Connection connection = DbConnect.dbConnect.getConnect();
                        String sql = "INSERT INTO chat (from_id, to_id, message, time) VALUES(?,?,?,?)";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, fromOpenId);
                        ps.setString(2, toOpenId);
                        ps.setString(3, message);
                        ps.setTimestamp(4, timestamp);
                        ps.executeUpdate();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }else {
                System.out.println("无效的http请求");
            }
        }
    }
}
