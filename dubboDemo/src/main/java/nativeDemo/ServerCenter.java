package nativeDemo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟dubbo+容器
 */
public class ServerCenter implements Server {
    //容器启动
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //模拟注册中心
    private static final Map<String, Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;//端口

    public ServerCenter(int port) {
        this.port = port;
    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void start() throws IOException {
        //使用ServerSocket监听
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("启动服务器");

        try {
            while (true) {
                //监听客户端TCP连接，然后封装成task给到线程池执行，就是典型的服务器监听
                isRunning = true;


                executor.execute(new ServiceTask(serverSocket.accept()));
            }
        }finally {
            serverSocket.close();
        }
    }

    //注册接口名、接口实现
    @Override
    public void registClass(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return port;
    }

    private static class ServiceTask implements Runnable {
        private Socket client = null;
        public ServiceTask(Socket socket) {
            client = socket;
        }

        @Override
        public void run() {
            ObjectInputStream inputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                //2、客户端的数据反序列化成对象，反射调用服务实现，获取执行结果
                inputStream = new ObjectInputStream(client.getInputStream());

                //下面的就是相当于 dubbo自定义的协议，怎么传输数据，现在只是简单的{ 服务名、方法名、参数的类Class、参数值}

                String serviceName = inputStream.readUTF();
                String methodName = inputStream.readUTF();
                Class<?>[] paramClass = (Class<?>[]) inputStream.readObject();
                Object[] arguments = (Object[]) inputStream.readObject();


                //反射获取实际对象

                //获取实现类
                Class serviceClass = serviceRegistry.get(serviceName);
                Method method = serviceClass.getMethod(methodName, paramClass);
                Object result = method.invoke(serviceClass.newInstance(), arguments);

                //3、反序列化结果，通过socket返回
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(result);

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
