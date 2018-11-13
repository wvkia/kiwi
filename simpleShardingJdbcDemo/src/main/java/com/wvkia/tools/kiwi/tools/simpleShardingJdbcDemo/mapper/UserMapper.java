package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.UserDo;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.example.UserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    List<UserDo> selectByExample(UserExample example);

    UserDo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserDo record, @Param("example") UserExample example);

    int updateByExample(@Param("record") UserDo record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);
}