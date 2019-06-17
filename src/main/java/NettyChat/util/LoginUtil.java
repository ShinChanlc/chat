//package NettyChat.util;
//
//import NettyChat.attribute.Attributes;
//import io.netty.channel.Channel;
//import io.netty.util.Attribute;
//
///**
// * Created by ShinChan on 2019/5/29.
// */
//public class LoginUtil {
//    public static void markAsLogin(Channel channel){
//        channel.attr(Attributes.LOGIN).set(true);
//    }
//
//    public static boolean hasLogin(Channel channel){
//        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
//        return loginAttr.get() != null;
//    }
//}
