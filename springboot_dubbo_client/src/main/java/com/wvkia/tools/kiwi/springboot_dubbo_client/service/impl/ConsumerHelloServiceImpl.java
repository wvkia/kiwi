package com.wvkia.tools.kiwi.springboot_dubbo_client.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wvkia.tools.kiwi.springboot_dubbo_client.service.ConsumerHelloService;
import com.wvkia.tools.kiwi.springboot_dubbo_server.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 * @date 2018/11/26
 */
@Component
public class ConsumerHelloServiceImpl implements ConsumerHelloService {
    @Reference(version = "1.0.0")
    private HelloService helloService;
    @Override
    public String sayHello() {
        String hello = helloService.sayHello();
        String say = hello + " ,consumer is me";
        System.out.println(say);
        return say;
    }
}
