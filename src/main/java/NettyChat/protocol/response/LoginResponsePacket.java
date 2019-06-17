package NettyChat.protocol.response;
import NettyChat.protocol.OffLineMessage;
import NettyChat.protocol.Packet;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static NettyChat.command.Command.LOGIN_RESPONSE;
/**
 * Created by ShinChan on 2019/5/28.
 */
public class LoginResponsePacket extends Packet{

    private String userId = "";
    private String userName = "";
    private boolean success = true;
    private String reason = "";
    private ArrayList<OffLineMessage> offLineMessages = new ArrayList<>();

    public LoginResponsePacket(){

    }

    public ArrayList<OffLineMessage> getOffLineMessages() {
        return offLineMessages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void addOffLineMessage(OffLineMessage offLineMessage){
        offLineMessages.add(offLineMessage);
    }

    @Override
    public Byte getCommand(){
        return LOGIN_RESPONSE;
    }

}
