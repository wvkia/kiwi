package ex02;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor2 {
    public void process(IRequest02 request, IResponse02 response) {
        String uri = request.getUri();

        //这里获取的只是servlet的名字，此时并不知道全限定名
        //传递格式是 /servlet/servletName
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        //URL类加载器，是ClassLoader的直接子类
        URLClassLoader loader = null;


        try {
            //创建URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT_SERVLET);


            //在一个servleet容器中，类加载器可以找到servlet的地方叫做资源库（respository）
            String repository = (new URL("file", null, classPath.getCanonicalPath() ).toString());

            urls[0] = new URL(repository);

            loader = new URLClassLoader(urls);
            System.out.println(urls[0].getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Class clazz = null;
        try {

            //加载一个类

            //TODO 暂未解决加载类的位置
            clazz = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Servlet servlet = null;
        try {

            //类进行是实例化
            servlet = (Servlet) clazz.newInstance();
            servlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
