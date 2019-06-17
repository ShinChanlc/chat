package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    private static final int MESSAGE_SIZE = 1024;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Client("127.0.0.1", 12345);
    }

    public Client(String host, int post) {
        try {
            socket = new Socket(host, post);
            if (socket.isConnected() == true) {
                System.out.println("连接成功");
                new Thread() {//开启一个接受数据的线程
                    @Override
                    public void run() {
                        super.run();
                        InputStream in;
                        try {
                            in = socket.getInputStream();
                            byte[] b;
                            while (true) {
                                b = new byte[MESSAGE_SIZE];
                                in.read(b);
                                System.out.println("接收到服务端消息：" + new String(b));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
            OutputStream out = null;
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String str = scanner.nextLine();
                out = socket.getOutputStream();
                out.write(str.getBytes());
                out.flush();
                if (str.equals("end")) {
                    System.exit(0);//关闭客户端
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
