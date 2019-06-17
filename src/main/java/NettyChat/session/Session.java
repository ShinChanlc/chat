package NettyChat.session;

/**
 * Created by ShinChan on 2019/5/29.
 */
public class Session {

    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Session(String openId){
        this.openId = openId;
    }

    @Override
    public String toString(){
        return openId;
    }
}
