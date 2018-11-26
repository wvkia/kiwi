package com.wvkia.tools.kiwi.springboot_dubbo_server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wvkia.tools.kiwi.springboot_dubbo_server.service.HelloService;

/**
 * @author wukai
 * @date 2018/11/26
 * 基于注解
 */
@Service(version = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "hello ,this is server";
    }
}
