package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ShinChan on 2019/5/27.
 */
public class ChatServer {
    private Selector selector = null;
    static final int post = 12306;
    private Charset charset = Charset.forName("UTF-8");
    private static HashMap<String, SocketChannel> map = new HashMap<>();
    private static boolean flag = false;
    private static HashSet<String> users = new HashSet<String>();
    private static String USER_EXIST = "system message: user exist, please change a name";
    private static String USER_CONTENT_SPILIT = "#@#";

    public void init() throws IOException{
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(post));
        //采用非阻塞的方式
        serverSocketChannel.configureBlocking(false);
        //注册到选择器上，设置为监听状态
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server is listening now...");

        while (true){
            int readyChannels = selector.select();
            if (readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();//可以通过这个方法，知道可用通道的集合
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey sk = (SelectionKey) keyIterator.next();
                keyIterator.remove();
                dealWithSelectionKey(serverSocketChannel,sk);
            }
        }
    }
    public void dealWithSelectionKey(ServerSocketChannel server,SelectionKey sk) throws IOException {
        if(sk.isAcceptable())
        {
            SocketChannel sc = server.accept();
            //非阻塞模式
            sc.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个SocketChannel处理
            sc.register(selector, SelectionKey.OP_READ);

            //将此对应的channel设置为准备接受其他客户端请求
            sk.interestOps(SelectionKey.OP_ACCEPT);
            System.out.println("Server is listening from client :" + sc.getRemoteAddress());
            sc.write(charset.encode("Please input fromId."));
        }
        //处理来自客户端的数据读取请求
        if(sk.isReadable())
        {
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel sc = (SocketChannel)sk.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try
            {
                while(sc.read(buff) > 0)
                {
                    buff.flip();
                    content.append(charset.decode(buff));

                }
                System.out.println("Server is listening from client " + sc.getRemoteAddress() + " ToId: " + content);
                //将此对应的channel设置为准备下一次接受数据
                sk.interestOps(SelectionKey.OP_READ);
            }
            catch (IOException io)
            {
                sk.cancel();
                if(sk.channel() != null)
                {
                    sk.channel().close();
                }
            }
            if(content.length() > 0)
            {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if(arrayContent != null && arrayContent.length ==1) {
                    String fromId = arrayContent[0];
                    map.put(fromId, sc);
//                    if(users.contains(name)) {
//                        sc.write(charset.encode(USER_EXIST));
//
//                    } else {
//                        users.add(name);
//                        int num = OnlineNum(selector);
//                        String message = "welcome "+name+" to chat room! Online numbers:"+num;
//                        BroadCast(selector, null, message);
//                    String message = "welcome fromId: " + fromId+ " to chat room!";

//                    }
                }
                //注册完了，发送消息
                else if(arrayContent != null && arrayContent.length >1) {
                    String toId = arrayContent[0];
                    String message = arrayContent[1];
                    message = "fromId:xxx" + " say " + message;

                    BroadCast(toId, message);
                }
            }

        }
    }
    //TODO 要是能检测下线，就不用这么统计了
    public static int OnlineNum(Selector selector) {
        int res = 0;
        for(SelectionKey key : selector.keys())
        {
            Channel targetchannel = key.channel();

            if(targetchannel instanceof SocketChannel)
            {
                res++;
            }
        }
        return res;
    }

    public void BroadCast(String toId, String content) throws IOException {
        //广播数据到所有的SocketChannel中
        try {
            map.get(toId  ).write(charset.encode(content));
        }catch (Exception e){
            System.out.println("你联系的用户不在线上！");
        }
//        for(SelectionKey key : selector.keys())
//        {
//            Channel targetchannel = key.channel();
//            //如果except不为空，不回发给发送此内容的客户端
//            if(targetchannel instanceof SocketChannel && targetchannel!=except)
//            {
//                SocketChannel dest = (SocketChannel)targetchannel;
//                dest.write(charset.encode(content));
//            }
//        }
    }

    public static void main(String[] args) throws IOException
    {
        new ChatServer().init();
    }
}
