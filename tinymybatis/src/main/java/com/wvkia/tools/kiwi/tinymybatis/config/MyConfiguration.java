package com.wvkia.tools.kiwi.tinymybatis.config;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyConfiguration {
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    public Connection build(String resource) {
        try {
            InputStream stream = loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            throw new RuntimeException(e+" "+ resource);
        }
    }
    private  Connection evalDataSource(Element node) throws ClassNotFoundException {
        if (!node.getName().equals("database")) {
            throw new RuntimeException("root should be database");
        }
        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        //获取属性节点
        for (Object item : node.elements("property")) {
            Element i = (Element) item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if (name == null || value == null) {
                throw new RuntimeException("[database]: <property> should contain name and value");
            }

            //赋值
            switch (name) {
                case "url" : url = value; break;
                case "username" : username = value; break;
                case "password" : password = value; break;
                case "driverClassName" : driverClassName = value; break;
                default : throw new RuntimeException("[database]: <property> unknown name");
            }
        }

        Class.forName(driverClassName);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    //获取property属性的值,如果有value值,则读取 没有设置value,则读取内容
    private  String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }



    public MapperBean readMapper(String path ) {
        MapperBean mapper = new MapperBean();
        try {
            InputStream stream = loader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();

            //开始解析一个xml
            mapper.setInterfaceName(root.attributeValue("nameSpace").trim());//设置namespacee为接口名
            List<Function> list = new ArrayList<>();//存储方法

            //遍历根节点所有子节点
            for (Iterator rootIterator = root.elementIterator(); rootIterator.hasNext(); ) {
                Function function = new Function();

                Element e = (Element) rootIterator.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                function.setSqlType(sqltype);
                function.setFuncName(funcName);
                function.setSql(sql);


                Object newInstance = null;
                try {
                    newInstance = Class.forName(resultType).newInstance();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                function.setResultType(newInstance);
                list.add(function);
            }
            mapper.setList(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapper;
    }


}
