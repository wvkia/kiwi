package com.wvkia.tools.kiwi.tinymybatis.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class MySqlSession {
    private Excutor excutor = new MyExcutor();
    private MyConfiguration myConfiguration = new MyConfiguration();


    public <T> T selectOne(String statement, Object parameter) {
        return excutor.query(statement, parameter);
    }


    //创建动态代理
    //tclass为接口class
    public <T> T getMapper(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new MyMapperProxy(myConfiguration, this));
    }


}

/**
 * 动态代理处理类
 */
class MyMapperProxy implements InvocationHandler {
    private MySqlSession mySqlSession;
    private MyConfiguration myConfiguration;

    public MyMapperProxy( MyConfiguration myConfiguration,MySqlSession mySqlSession) {
        this.mySqlSession = mySqlSession;
        this.myConfiguration = myConfiguration;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = myConfiguration.readMapper("pojoDao.xml");
        if (!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())) {
            return null;
        }
        List<Function> list = readMapper.getList();
        for (Function func : list) {
            if (method.getName().equals(func.getFuncName())) {
                return mySqlSession.selectOne(func.getSql(), String.valueOf(args[0]));
            }
        }
        return null;
    }
}
