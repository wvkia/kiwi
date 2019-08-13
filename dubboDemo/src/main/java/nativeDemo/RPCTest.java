package nativeDemo;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RPCTest {
    public static void main(String[] args) throws IOException {
        Server server = new ServerCenter(8088);
        server.registClass(HelloService.class, HelloServiceImpl.class);
//        server.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //因为这个会一直阻塞，所以需要放到单独线程跑
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        while (!server.isRunning()) {
            Thread.yield();
        }

        HelloService helloService = RPCClient.getRemoteProxy(HelloService.class, new InetSocketAddress("localhost", 8088));

        System.out.println(helloService.hi("asdf"));
    }
}
