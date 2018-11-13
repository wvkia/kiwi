package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.manager.impl;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.manager.UserManager;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper.UserMapper;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.UserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManagerImpl implements UserManager {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDo getById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
