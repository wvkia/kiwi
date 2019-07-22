package com.wvkia.tools.kiwi.tinymybatis;


import com.wvkia.tools.kiwi.tinymybatis.config.MySqlSession;

public class TestMyBatis {
    public static void main(String[] args) {
        MySqlSession sqlsession = new MySqlSession();
        PoJoMapper mapper = sqlsession.getMapper(PoJoMapper.class);
        PoJo poJo = mapper.getById("1");
        System.out.println(poJo);
    }
}
