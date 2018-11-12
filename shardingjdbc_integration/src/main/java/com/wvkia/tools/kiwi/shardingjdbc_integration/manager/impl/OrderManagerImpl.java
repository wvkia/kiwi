package com.wvkia.tools.kiwi.shardingjdbc_integration.manager.impl;

import com.wvkia.tools.kiwi.shardingjdbc_integration.manager.OrderManager;
import com.wvkia.tools.kiwi.shardingjdbc_integration.mapper.OrderDao;
import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.OrderDo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class OrderManagerImpl implements OrderManager {
    @Autowired
    private OrderDao orderDao;
    @Override
    public OrderDo getById(String id) {
        return orderDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(OrderDo record) {
        orderDao.updateByPrimaryKey(record);
    }

    @Override
    public void deleteById(String id) {
        orderDao.deleteByPrimaryKey(id);
    }

    @Override
    public void create(OrderDo record) {
        orderDao.insert(record);
    }
}
