package com.wvkia.tools.kiwi.springboot_dubbo_client.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.wvkia.tools.kiwi.springboot_dubbo_client.service.ConsumerHelloService;
import com.wvkia.tools.kiwi.springboot_dubbo_server.service.HelloService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 * @date 2018/11/26
 * RpcContext是一个ThreadLocal的临时状态记录器，当接收到RPC请求，或发起RPC请求时，RpcContext的状态都会变化。
 * 比如A调用B，B再调用C，则B机器上，在B调用C之前，RpcContext记录的是A调用B的信息，在B调用C之后，RpcContext记录的是B调用C。
 */
@Component
@Log
public class ConsumerHelloServiceImpl implements ConsumerHelloService {
    @Reference(version = "1.0.0")
    private HelloService helloService;
    @Override
    public String sayHello() {
        String hello = helloService.sayHello();

        //获取dubbo信息
        boolean isConsumer = RpcContext.getContext().isConsumerSide();
        boolean isProvider = RpcContext.getContext().isProviderSide();

        log.info("isConsumer : " + isConsumer + " isProvider: " + isProvider);
        log.info("远程ip removeIp = "+RpcContext.getContext().getRemoteAddressString()+",方法名method = "+RpcContext.getContext().getMethodName());

        String say = hello + " ,consumer is me";
        System.out.println(say);
        return say;
    }
}
