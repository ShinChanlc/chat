package NettyChat.protocol;

import lombok.Data;

/**
 * Created by ShinChan on 2019/5/28.
 */

@Data
public abstract class Packet {

    private Byte version = 1;

    public abstract Byte getCommand();

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}
