package chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    List<ReceiveThread> receiveList = new ArrayList<>();
    private final static int MESSAGE_SIZE = 1024;
    int num = 0;//客户端编号
    private int post;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Server(12345);
    }

    Server(int post){
        this.post = post;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(post);
            while(true){
                Socket socket = serverSocket.accept();
                //在其他线程处理接收来自客户端的消息
                System.out.println("连接上客户端："+num);
                ReceiveThread receiveThread = new ReceiveThread(socket, num);
                receiveThread.start();
                receiveList.add(receiveThread);
                //有客户端上线，服务器就通知其他客户端
                String notice = "有新客户端上线，现在在线客户端有：客户端：";
                for (ReceiveThread thread: receiveList) {
                    notice = notice + " " + thread.num;
                }
                for(ReceiveThread thread: receiveList){
                    new SendThread(thread.socket, notice).start();
                }
                num++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    class ReceiveThread extends Thread {
        int num;
        Socket socket;//客户端对应的套接字
        boolean continueReceive = true;//标识是否还维持连接需要接收

        public ReceiveThread(Socket socket, int num) {
            this.socket = socket;
            this.num = num;
            try {
                //给连接上的客户端发送，分配的客户端编号的通知
                socket.getOutputStream().write(("你的客户端编号是" + num).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            //接收客户端发送的消息
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                byte[] b;
                while (continueReceive) {
                    b = new byte[MESSAGE_SIZE];
                    inputStream.read(b);
                    b = splitByte(b);//去掉数组无用部分
                    //发送end的客户端断开连接
                    if (new String(b).equals("end")) {
                        continueReceive = false;
                        receiveList.remove(this);
                        //通知其他客户端
                        String message = "客户端" + num + "连接断开\n" +
                                "现在在线的有，客户端：";
                        for (ReceiveThread receiveThread : receiveList) {
                            message = message + " " + receiveThread.num;
                        }
                        System.out.println(message);
                        for (ReceiveThread receiveThread : receiveList) {
                            new SendThread(receiveThread.socket, message).start();
                        }
                    } else {
                        try {
                            String[] data = new String(b).split(" ", 2);//以第一个空格，将字符串分成两个部分
                            int clientNum = Integer.parseInt(data[0]);//转换为数字，即客户端编号数字
                            //将消息发送给指定客户端
                            for (ReceiveThread receiveThread : receiveList) {
                                if (receiveThread.num == clientNum) {
                                    new SendThread(receiveThread.socket, "客户端"+num+"发消息："+data[1]).start();
                                    System.out.println("客户端" + num + "发送消息到客户端" + receiveThread.num + ": " + data[1]);
                                }
                            }
                        } catch (Exception e) {
                            new SendThread(socket, "输入错误，请重新输入").start();
                            System.out.println("客户端输入错误");
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {//关闭资源
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SendThread extends Thread {
        Socket socket;
        String str;

        public SendThread(Socket socket, String str) {
            this.socket = socket;
            this.str = str;
        }

        @Override
        public void run() {
            super.run();
            try {
                socket.getOutputStream().write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //去除byte数组多余部分
    private   byte[] splitByte(byte b[]) {
        int i = 0;
        for(;i<b.length;i++) {
            if (b[i] == 0) {
                break;
            }
        }
        byte[] b2 = new byte[i];
        for (int j = 0; j <i ; j++) {
            b2[j] = b[j];
        }
        return b2;
    }
}
