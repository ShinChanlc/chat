package NettyChat.util;

import NettyChat.attribute.Attributes;
import NettyChat.session.Session;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ShinChan on 2019/5/30.
 */
public class SessionUtil {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel){
        userIdChannelMap.put(session.getOpenId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel){
        if (hasLogin(channel)){
            userIdChannelMap.remove(getSession(channel).getOpenId());
        }
    }

    public static boolean hasLogin(Channel channel){
        Attribute<Session> attribute = channel.attr(Attributes.SESSION);
        return attribute.get() != null;
    }

    public static Session getSession(Channel channel){
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String openId) {

        try {
            Channel channel = userIdChannelMap.get(openId);
            return channel;
        }catch (Exception e){
            return null;
        }
    }
}
