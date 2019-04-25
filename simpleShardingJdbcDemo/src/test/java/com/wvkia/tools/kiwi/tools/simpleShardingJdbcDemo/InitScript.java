package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper.OrderMapper;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper.UserMapper;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.OrderDo;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.UserDo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * @author wukai
 * @date 2018/11/23
 * 初始化脚本
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitScript {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    private static Random random = new Random(47);

    @Before
    public  void initConfig() {
        for (int i = 0; i < 10; i++) {
            OrderDo orderDo = new OrderDo();
            orderDo.setId(i + "_id_");
            orderDo.setName(i + "_name_");
            orderMapper.insert(orderDo);
        }

        for (int i = 0; i < 10; i++) {
            UserDo userDo = new UserDo();
            userDo.setId(i);
            userDo.setName(i+"_name_");
            userMapper.insert(userDo);
        }
    }

    @Test
    public void testInsert() {

    }

}
