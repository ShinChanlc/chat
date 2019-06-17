//package chat;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.Socket;
//
///**
// * Created by ShinChan on 2019/5/27.
// */
//public class ChatSocket extends Thread {
//    Socket socket;
//
//    public ChatSocket(Socket sc){
//        this.socket = sc;
//    }
//
//    public void out(String out){
//        try {
//            socket.getOutputStream().write((out+"\n").getBytes("UTF-8"));
//        }catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("断开了一个客户端连接");
//            chatManager.getchaChatManager().remove(this);
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run(){
//        out("您已经连接到服务器");
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            String line = null;
//            while ((line  =  br.readLine()) != null){
//                ChatManager.getchaChatManager().publish(this, line);
//            }
//            br.close();
//            System.out.println("断开一个客户端连接");
//            ChatManager.getchaChatManager().remove(this);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("断开一");
//            e.printStackTrace();
//        }
//    }
//}
