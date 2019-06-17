package NettyChat.protocol.response;

import NettyChat.protocol.Packet;

import java.util.Date;

import static NettyChat.command.Command.MESSAGE_RESPONSE;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class MessageResponsePacket extends Packet {

    private String fromUserId;
    private String message;
    private Date sendTime;
    private String toUserId;

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Byte getCommand(){
        return MESSAGE_RESPONSE;
    }
}
