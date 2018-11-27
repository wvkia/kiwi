package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.Fanout;

import com.rabbitmq.client.*;
import com.wvkia.tools.kiwi.springboot_rabbitmq_demo.nativeS.RabbitMqFinalConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author wukai
 * @date 2018/11/26
 */
@Slf4j
public class Receiver {
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

            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue, EXCHANGE, "");
            log.info("receive wait message");
            Consumer consumer=new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    String message = new String(body, 0, body.length, "UTF-8");
                    log.info("receive message : " + message);
                }
            };
            channel.basicConsume(queue, true, consumer);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
