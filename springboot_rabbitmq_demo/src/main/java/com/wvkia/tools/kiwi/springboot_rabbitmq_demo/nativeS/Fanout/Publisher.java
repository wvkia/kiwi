package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.Fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.RabbitMqFinalConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wukai
 * @date 2018/11/26
 */
@Slf4j
public class Publisher {
    public static final String EXCHANGE = "publisher";

    public static void main(String[] args) {
        //1. 创建connecitonFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //2. 配置连接信息
        connectionFactory.setHost(RabbitMqFinalConfig.HOST);
        connectionFactory.setUsername(RabbitMqFinalConfig.USER_NAME);
        connectionFactory.setPassword(RabbitMqFinalConfig.PASSWORD);
        Connection connection = null;
        Channel channel = null;
        try {
            //3. 新建连接
            connection = connectionFactory.newConnection();
            //4. 获取channel
            channel = connection.createChannel();

            //设置exchangeType
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);



            //延时发送
            String message = getMessage();
            log.info("publisher will send message: " + message);
            channel.basicPublish(EXCHANGE, "", null, message.getBytes());

        } catch (TimeoutException e) {
            log.error("connection io exception");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("connection timeout exception");
            e.printStackTrace();
        }finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getMessage() {
        try {
            TimeUnit.MICROSECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(System.currentTimeMillis());
    }

}
