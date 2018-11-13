package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.OrderDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testInsert() {
        for (int i = 0; i < 200; i++) {
            OrderDo orderDo = new OrderDo();
            orderDo.setId(i+"_id_"+12345);
            orderDo.setName(i+"_name");
            orderMapper.insert(orderDo);
        }

    }
    @Test
    public void testGet() {
        String id = "3_id_12345";
        System.out.println(orderMapper.selectByPrimaryKey(id));
    }
}
