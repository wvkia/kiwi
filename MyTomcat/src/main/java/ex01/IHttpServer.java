package ex01;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 第一版就是简单的利用ServereSocket监听Socket连接，然后返回数据
 */
public class IHttpServer {

    //WEB_ROOT是存储HTML和饿其他文件的地方
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "/MyTomcat/src/main/resources/webroot";

    /**
     * 停止服务器命令
     */
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutDown = false;//服务器是否已经关闭

    public static void main(String[] args) {
        IHttpServer server = new IHttpServer();
        server.await();
    }

    /**
     * 监听客户端请求
     */
    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!shutDown) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();


                //创建Request请求
                IRequest request = new IRequest(inputStream);
                request.parse();

                //创建Responsee对象
                IResponse response = new IResponse(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                //关闭socket
                socket.close();

                shutDown = request.getUri().equals(SHUTDOWN_COMMAND);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
