package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserMapper {
    int insert(UserDo userDo);

    int deleteById(int id);

    UserDo selectById(int id);

    List<UserDo> selectLikeName(String name);

    void updateName(@Param("sourceName") String sourceName, @Param("destName") String destName);

    void updateById(@Param("id") int id, @Param("name") String name);
}