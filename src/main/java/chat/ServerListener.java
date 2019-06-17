//package chat;
//
//import javax.swing.*;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
///**
// * Created by ShinChan on 2019/5/27.
// */
//public class ServerListener extends Thread {
//    @Override
//    public void run(){
//        try {
//            ServerSocket serverSocket =new ServerSocket(12345);
//            while (true){
//                Socket socket = serverSocket.accept();
//                JOptionPane.showConfirmDialog(null, "有客户端连接到了本机的12345端口");
//                ChatSocket cs = new ChatSocket(socket);
//                cs.start();
//                ChatManager.getChatManager().add(cs);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//}
