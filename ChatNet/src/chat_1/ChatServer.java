package chat_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    //保存所有客户端连接
    public static List<Socket> clientList = new CopyOnWriteArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        int port = 8888;
        ServerSocket serverSocket = new ServerSocket( port);
        while (true) {
            Socket clntSockt = serverSocket.accept();
            System.out.println("客户端IP ：" + clntSockt+"已连接");
            clientList.add(clntSockt);
            //新建一个线程处理
            new Thread(new ClientChatRunnable(clntSockt)).start();
        }
    }

}
