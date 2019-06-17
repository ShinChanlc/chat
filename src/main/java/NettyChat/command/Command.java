package NettyChat.command;

/**
 * Created by ShinChan on 2019/5/28.
 */
public interface Command {
    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Byte OFF_LINE_MESSAGE_RESPONSE = 5;
}
