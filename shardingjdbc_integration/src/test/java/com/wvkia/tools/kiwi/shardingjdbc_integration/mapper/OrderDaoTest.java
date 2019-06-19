package com.wvkia.tools.kiwi.shardingjdbc_integration.mapper;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.OrderDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

/**
 * @author wukai
 * @date 2019/4/25
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void insert() {
        OrderDo orderDo = new OrderDo();
        orderDo.setId("0");
        orderDo.setName(orderDo.getId());

        orderDao.insert(orderDo);
    }

    @Test
    public void get() {
        OrderDo orderDo = orderDao.selectByPrimaryKey("test_readwrite_split");
        Assert.notNull(orderDo);
    }
}