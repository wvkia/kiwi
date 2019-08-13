package ex02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer2 {
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutDown = false;

    public static void main(String[] args) {
        HttpServer2 server = new HttpServer2();
        server.await();
    }
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
                IRequest02 request = new IRequest02(inputStream);
                request.parse();

                //创建Responsee对象
                IResponse02 response = new IResponse02(outputStream);
                response.setRequest(request);


                //判断是servlet还是静态资源
                if (request.getUri().startsWith("/servlet/")) {

                    //servlet处理器
                    ServletProcessor2 processor = new ServletProcessor2();
                    processor.process(request, response);
                }else {

                    //静态资源处理器
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }

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
