package NettyChat.protocol;

import java.util.Date;

/**
 * Created by ShinChan on 2019/6/2.
 */
public class OffLineMessage {

    private String fromId = "";
    private String toId = "";
    private String Message = "";
    private Date sendTime = null;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String toString(){
        return fromId + toId + Message;
    }
}
