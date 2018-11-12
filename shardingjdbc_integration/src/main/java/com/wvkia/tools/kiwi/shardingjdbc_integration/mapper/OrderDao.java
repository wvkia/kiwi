package com.wvkia.tools.kiwi.shardingjdbc_integration.mapper;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.OrderDo;
import com.wvkia.tools.kiwi.shardingjdbc_integration.model.example.OrderDoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OrderDao {
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