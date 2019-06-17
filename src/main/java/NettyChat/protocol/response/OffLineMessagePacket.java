package NettyChat.protocol.response;

import NettyChat.protocol.OffLineMessage;
import NettyChat.protocol.Packet;

import java.util.ArrayList;

import static NettyChat.command.Command.OFF_LINE_MESSAGE_RESPONSE;

/**
 * Created by ShinChan on 2019/6/2.
 */
public class OffLineMessagePacket extends Packet {
    private ArrayList<OffLineMessage> offLineMessages;

    public void addOffLineMessage(OffLineMessage offLineMessage){
        offLineMessages.add(offLineMessage);
    }



    public Byte getCommand(){
        return OFF_LINE_MESSAGE_RESPONSE;
    }
}
