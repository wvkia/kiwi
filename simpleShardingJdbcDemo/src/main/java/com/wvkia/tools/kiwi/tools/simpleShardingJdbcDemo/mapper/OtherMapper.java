package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.simpleShardingJdbcDemo.model.domain.OtherDo;
import com.wvkia.tools.kiwi.simpleShardingJdbcDemo.model.example.OtherExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OtherMapper {
    int countByExample(OtherExample example);

    int deleteByExample(OtherExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OtherDo record);

    int insertSelective(OtherDo record);

    List<OtherDo> selectByExample(OtherExample example);

    OtherDo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OtherDo record, @Param("example") OtherExample example);

    int updateByExample(@Param("record") OtherDo record, @Param("example") OtherExample example);

    int updateByPrimaryKeySelective(OtherDo record);

    int updateByPrimaryKey(OtherDo record);
}