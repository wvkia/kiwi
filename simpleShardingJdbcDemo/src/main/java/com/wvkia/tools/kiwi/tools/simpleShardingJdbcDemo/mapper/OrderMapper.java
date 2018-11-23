package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.OrderDo;

import java.util.List;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.example.OrderDoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OrderMapper {
    int countByExample(OrderDoExample example);

    int deleteByExample(OrderDoExample example);

    int deleteByPrimaryKey(String id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    List<OrderDo> selectByExample(OrderDoExample example);

    OrderDo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OrderDo record, @Param("example") OrderDoExample example);

    int updateByExample(@Param("record") OrderDo record, @Param("example") OrderDoExample example);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}