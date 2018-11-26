package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.UserDo;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.example.UserExample;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
        UserDo userDo = userMapper.selectByPrimaryKey(id);
        System.out.println(userDo);
        Assert.assertNotNull(userDo);
    }

    @Test
    public void testGetname() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo("name2");

        List<UserDo> userDoList = userMapper.selectByExample(example);
        System.out.println(userDoList);
        Assert.assertNotNull(userDoList);
    }

    @Test
    public void testCount() {
        int count = userMapper.countByExample(null);
        System.out.println(count);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testLike() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%name%");

        List<UserDo> userDoList = userMapper.selectByExample(example);
        System.out.println(userDoList);
        System.out.println(userDoList.size());
        Assert.assertNotNull(userDoList);
    }

    @Test
    public void testUpdate() {

        //manager库
        int id = 0;
        UserDo userDo = new UserDo();
        userDo.setId(0);
        userDo.setName("namammaam");

        userMapper.updateByPrimaryKeySelective(userDo);


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
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2);

        UserExample.Criteria criteria1 = example.createCriteria();
        criteria1.andNameEqualTo("name20");
        example.or(criteria1);


        List<UserDo> userDoList = userMapper.selectByExample(example);
        Assert.assertNotNull(userDoList);
        System.out.println(userDoList);
    }

    /**
     * 多库写
     */
    @Test
    public void testUpdateMuch() {

        UserDo userDo = new UserDo();
        userDo.setName("name测试");

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%name%");


        userMapper.updateByExampleSelective(userDo, example);



    }


}
