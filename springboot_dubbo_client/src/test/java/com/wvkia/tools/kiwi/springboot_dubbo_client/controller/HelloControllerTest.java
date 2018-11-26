package com.wvkia.tools.kiwi.springboot_dubbo_client.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wukai
 * @date 2018/11/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest {
    @Autowired
    private HelloController helloController;

    @Test
    public void testSayHello() {
        System.out.println(helloController.sayHello());
    }
}
