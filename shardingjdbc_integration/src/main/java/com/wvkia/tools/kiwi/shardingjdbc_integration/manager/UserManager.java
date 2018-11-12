package com.wvkia.tools.kiwi.shardingjdbc_integration.manager;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.UserDo;

public interface UserManager {

    UserDo getById(int id);

    void update(UserDo record);

    void deleteById(int id);

    void create(UserDo record);

}
