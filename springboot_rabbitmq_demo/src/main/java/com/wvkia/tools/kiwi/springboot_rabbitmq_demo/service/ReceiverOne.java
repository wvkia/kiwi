package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 * @date 2018/11/27
 */
@Component
@Slf4j
@RabbitListener(queues = "queue_one")
public class ReceiverOne {
    @RabbitHandler
    public void process(String message) {
        log.info("receiver_one get : " + message);
    }
}
