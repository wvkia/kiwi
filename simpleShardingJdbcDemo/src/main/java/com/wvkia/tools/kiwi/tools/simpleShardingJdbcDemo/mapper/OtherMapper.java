package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.OtherDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OtherMapper {
    int insert(OtherDo record);

    void deleteById(String id);

    List<OtherDo> selectName(String name);

    List<OtherDo> selectOr();
}