package NettyChat.protocol.request;

import NettyChat.protocol.Packet;

import java.util.Date;

import static NettyChat.command.Command.MESSAGE_REQUEST;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class MessageRequestPacket extends Packet {
    private String message;
    private String toUserId;
    private String fromUserId;
    private Date sendTime;
    public String getMessage() {
        return message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public MessageRequestPacket(String fromUserId, String toUserId, String message, Date sendTime){
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.sendTime = sendTime;
    }

    @Override
    public Byte getCommand(){

        return MESSAGE_REQUEST;
    }
}
