package NettyChat.protocol.request;

import NettyChat.protocol.Packet;
import lombok.Data;

/**
 * Created by ShinChan on 2019/5/28.
 */
import static NettyChat.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet{
    private String userId;
    private String userName;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    @Override
    public Byte getCommand(){
        return LOGIN_REQUEST;
    }
}
