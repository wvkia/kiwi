package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.OrderDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OrderMapper {
    int insert(OrderDo record);

    int deleteById(String id);

    OrderDo selectById(String id);

    List<OrderDo> selectByUserId(String userId);
}