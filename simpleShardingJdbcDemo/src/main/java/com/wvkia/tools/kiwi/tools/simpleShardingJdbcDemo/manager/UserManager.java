package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.manager;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.UserDo;

public interface UserManager {
    UserDo getById(int id);
}
