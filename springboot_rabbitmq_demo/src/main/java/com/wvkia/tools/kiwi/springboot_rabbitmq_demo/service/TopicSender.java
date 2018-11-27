package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.service;

import com.wvkia.tools.kiwi.springboot_rabbitmq_demo.config.TopicRabbitMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 * @date 2018/11/27
 */
@Component
@Slf4j
public class TopicSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    //两个接受者都能接收
    public void send_one() {
        String context = "消息 = Message one";
        log.info("发送: " + context);
        this.rabbitTemplate.convertAndSend(TopicRabbitMQ.EXCHANGE, "topic.one", context);
    }

    //只有receiverTwo可以接收
    public void send_two() {
        String context = "消息 = Message two";
        log.info("发送: " + context);
        this.rabbitTemplate.convertAndSend(TopicRabbitMQ.EXCHANGE, "topic.two", context);
    }

}
