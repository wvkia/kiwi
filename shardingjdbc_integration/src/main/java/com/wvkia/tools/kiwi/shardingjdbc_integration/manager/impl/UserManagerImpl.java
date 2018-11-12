package com.wvkia.tools.kiwi.shardingjdbc_integration.manager.impl;

import com.wvkia.tools.kiwi.shardingjdbc_integration.manager.UserManager;
import com.wvkia.tools.kiwi.shardingjdbc_integration.mapper.UserDao;
import com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain.UserDo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserManagerImpl implements UserManager {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDo getById(int id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(UserDo record) {
        userDao.updateByPrimaryKey(record);
    }

    @Override
    public void deleteById(int id) {
        userDao.deleteByPrimaryKey(id);
    }

    @Override
    public void create(UserDo record) {
        userDao.insert(record);

    }
}
