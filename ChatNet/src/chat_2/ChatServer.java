package chat_2;

import chat_1.ClientChatRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    //保存所有客户端连接
    public static List<Socket> clientList = new CopyOnWriteArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        int port = 8888;
        ServerSocket serverSocket = new ServerSocket( port);
        //线程池处理线程,默认初始线程100个
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while (true) {
            Socket clntSockt = serverSocket.accept();
            System.out.println("客户端IP ：" + clntSockt);
            clientList.add(clntSockt);
            threadPool.execute(new ClientChatRunnable(clntSockt));

        }
    }
}
