package com.wvkia.tools.kiwi.springboot_rabbitmq_demo;

import com.wvkia.tools.kiwi.springboot_rabbitmq_demo.service.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wukai
 * @date 2018/11/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {
    @Autowired
    private TopicSender topicSender;

    @Test
    public void send() {
        topicSender.send_one();
        topicSender.send_two();

    }
}
