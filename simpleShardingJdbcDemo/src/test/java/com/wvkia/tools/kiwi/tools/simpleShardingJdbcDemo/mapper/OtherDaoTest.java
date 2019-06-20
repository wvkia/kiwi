package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.mapper;

import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.model.OtherDo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OtherDaoTest {
    @Autowired
    private OtherMapper otherMapper;

    @Test
    public void testGetOther() {
        String name = "sdf";
        List<OtherDo> otherDos = otherMapper.selectName(name);
        Assert.assertNotNull(otherDos);
        System.out.println(otherDos);
    }

}
