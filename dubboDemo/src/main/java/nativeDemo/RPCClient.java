package nativeDemo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 客户端的远程代理对象
 *
 * 就是dubbo客户端，调用dubbo接口时，做的工作
 * @param <T>
 */
public class RPCClient<T> {

    //第一个参数是远程调用的接口名，第二是对应接口的IP地址，我们这里要做的就是把这个接口生成一个代理对象，
    // 当接口方法被调用就发送RPC协议信息给远程IP对应的服务器，让他们执行，返回结果再序列化成对应的结果
    public static <T> T getRemoteProxy(final Class serviceInterface, InetSocketAddress address) {
        //1、创建接口代理对象
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream objectOutputStream = null;
                ObjectInputStream objectInputStream = null;
                try {
                    //2、创建本地socket，用于发送数据到远程IP
                    socket = new Socket();
                    socket.connect(address);

                    //3、将远程服务需要的接口类、参数等就是dubbo协议的数据编码后给服务提供方
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeUTF(serviceInterface.getName());
                    objectOutputStream.writeUTF(method.getName());
                    objectOutputStream.writeObject(method.getParameterTypes());
                    objectOutputStream.writeObject(args);

                    //4、同步阻塞等待服务器返回应答
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                    return objectInputStream.readObject();
                } finally {
                    if (socket != null) socket.close();
                    if (objectOutputStream != null) objectOutputStream.close();
                    if (objectInputStream != null) objectInputStream.close();
                }
            }
        });
    }
}
