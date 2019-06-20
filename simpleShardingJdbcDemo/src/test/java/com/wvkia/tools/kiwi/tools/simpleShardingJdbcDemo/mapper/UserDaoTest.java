package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.UserDo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 对于不是分库的字段查询，都是查询全部的库，全部的表，然后做聚合操作
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserMapper userMapper;

    /**
     * 支持分库字段get
     */
    @Test
    public void testGet() {
        int id = 0;
        UserDo userDos = userMapper.selectById(id);
        System.out.println(userDos);
        Assert.assertNotNull(userDos);
    }

    @Test
    public void testUpdate() {

        int id = 0;
        userMapper.updateById(id, "测试");
        UserDo userDo = userMapper.selectById(id);
        Assert.assertEquals(userDo.getName(), "测试");


    }

    @Test
    public void testInsert() {
        int id = 28;
        UserDo userDo = new UserDo();
        userDo.setId(id);
        userDo.setName("nameid28");

        userMapper.insert(userDo);
    }

    /**
     * 不支持or语句
     */
    @Test
    public void testOr() {

//        List<UserDo> userDoList = userMapper.selectOr(example);
//        Assert.assertNotNull(userDoList);
//        System.out.println(userDoList);
    }

    /**
     * 多库写
     */
    @Test
    public void testUpdateMuch() {
        String sourceName = "name_";
        String destName = "测试";

        userMapper.updateName(sourceName, destName);
        List<UserDo> list = userMapper.selectLikeName(sourceName);
        for (UserDo userDo : list) {
            Assert.assertEquals(userDo.getName(), destName);
        }



    }


}
