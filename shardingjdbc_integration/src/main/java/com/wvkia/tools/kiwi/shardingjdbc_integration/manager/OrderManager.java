package com.wvkia.tools.kiwi.shardingjdbc_integration.manager;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.OrderDo;

public interface OrderManager {

    OrderDo getById(String id);

    void update(OrderDo record);

    void deleteById(String id);

    void create(OrderDo record);
}
