//package chat;
//
//import java.util.Vector;
//
///**
// * Created by ShinChan on 2019/5/27.
// */
//public class ChatManager {
//    private ChatManager(){}
//
//    private static final ChatManager instance = new ChatManager();
//
//    public static ChatManager getChatManager(){
//        return instance;
//    }
//
//    Vector<ChatSocket> vector = new Vector<>();
//
//    public void add(ChatSocket cs){
//        vector.add(cs);
//    }
//
//    public void remove(ChatSocket cs){
//        vector.remove(cs);
//    }
//
//    public void publish(ChatSocket cs, String chatinfo){
//        for (ChatSocket chatSocket: vector
//             ) {
//            if(!chatSocket.equals(cs)){
//                chatSocket.out(chatinfo);
//            }
//        }
//    }
//}
