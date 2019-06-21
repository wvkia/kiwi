package com.wvkia.tools.kiwi.shardingjdbc_integration.mapper;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.OrderDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wukai
 * @date 2019/4/25
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insert() {
        OrderDo orderDo = new OrderDo();
        orderDo.setId("0");
        orderDo.setName(orderDo.getId());

        orderMapper.insert(orderDo);
    }

    @Test
    public void get() {

    }
}