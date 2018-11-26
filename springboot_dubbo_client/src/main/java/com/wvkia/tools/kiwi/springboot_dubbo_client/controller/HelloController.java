package com.wvkia.tools.kiwi.springboot_dubbo_client.controller;

import com.wvkia.tools.kiwi.springboot_dubbo_client.service.ConsumerHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wukai
 * @date 2018/11/26
 */
@RestController
public class HelloController {
    @Autowired
    private ConsumerHelloService consumerHelloService;

    @GetMapping("sayHello")
    public String sayHello() {
        return consumerHelloService.sayHello();
    }

}
