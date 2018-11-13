package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.domain.OtherDo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OtherDaoTest {
    @Autowired
    private OtherMapper otherMapper;

    @Test
    public void testGetOther() {
        int id=1;
        OtherDo otherDo = otherMapper.selectByPrimaryKey(id);
        Assert.assertNotNull(otherDo);
        System.out.println(otherDo);
    }

}
