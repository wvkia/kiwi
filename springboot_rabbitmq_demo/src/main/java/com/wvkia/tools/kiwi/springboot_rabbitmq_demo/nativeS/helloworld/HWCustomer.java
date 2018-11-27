package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.helloworld;

import com.rabbitmq.client.*;
import com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.RabbitMqFinalConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wukai
 * @date 2018/11/26
 * 消费者
 *
 */
@Slf4j
public class HWCustomer {
    public static final String QUEUE = "FIRST_QUEUE";

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
            //5. 配置queue
            //幂等性，就是如果不存在就创建，如果存在不影响
            channel.queueDeclare(QUEUE, false, false, false, null);


            log.info("constomer wait message");
            //覆盖handlerDeliver方法，回调新消息
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String message = new String(body, 0, body.length, "UTF-8");
                    log.info("收到productor消息 = " + message);
                }
            };
            channel.basicConsume(QUEUE, true, consumer);
        } catch (TimeoutException e) {
            log.error("发送消息IO异常");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("发送消息超时");
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
}
