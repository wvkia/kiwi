package com.wvkia.tools.kiwi.springboot_dubbo_server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.wvkia.tools.kiwi.springboot_dubbo_server.service.HelloService;
import lombok.extern.java.Log;

/**
 * @author wukai
 * @date 2018/11/26
 * 基于注解
 */
@Log
@Service(version = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        //获取dubbo信息
        boolean isConsumer = RpcContext.getContext().isConsumerSide();
        boolean isProvider = RpcContext.getContext().isProviderSide();Integer

        log.info("isConsumer : " + isConsumer + " isProvider: " + isProvider);
        log.info("远程ip removeIp = "+RpcContext.getContext().getRemoteAddressString()+",方法名method = "+RpcContext.getContext().getMethodName());

        return "hello ,this is server";
    }
}
