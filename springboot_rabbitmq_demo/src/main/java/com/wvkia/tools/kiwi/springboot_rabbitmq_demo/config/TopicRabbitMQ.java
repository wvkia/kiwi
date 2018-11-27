package com.wvkia.tools.kiwi.springboot_rabbitmq_demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wukai
 * @date 2018/11/27
 */
@Configuration
public class TopicRabbitMQ {
    public static final String TOPIC_ONE = "queue_one";
    public static final String TOPIC_TWO = "queue_two";

    public static final String EXCHANGE = "exchange_topic";



    @Bean
    public Queue queue_one() {
        return new Queue(TOPIC_ONE);
    }

    @Bean
    public Queue queue_two() {
        return new Queue(TOPIC_TWO);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding bindingExchangeOne(Queue queue_one, TopicExchange exchange) {
        return BindingBuilder.bind(queue_one).to(exchange).with("topic.one");
    }

    //正则匹配
    @Bean
    Binding bindingExchangeTwo(Queue queue_two, TopicExchange exchange) {
        return BindingBuilder.bind(queue_two).to(exchange).with("topic.#");
    }


}
