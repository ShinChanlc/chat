package NettyChat.attribute;

import NettyChat.session.Session;
import io.netty.util.AttributeKey;

/**
 * Created by ShinChan on 2019/5/29.
 */
public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
