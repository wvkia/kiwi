package nativeDemo;

import java.io.IOException;

//模拟服务中心
//其实就是模拟dubbo+spring框架
public interface Server {
    void stop();

    void start() throws IOException;

    void registClass(Class serviceInterface, Class impl);

    boolean isRunning();

    int getPort();


}
