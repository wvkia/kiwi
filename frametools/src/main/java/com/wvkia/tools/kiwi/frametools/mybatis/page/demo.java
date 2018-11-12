package com.wvkia.tools.kiwi.frametools.mybatis.page;

import com.google.common.collect.Lists;

import java.util.List;

public class demo {
    public static void main(String[] args) {
        int page=1;
        int pageSize=20;

        //启动分页查询
        PageHelper.startPage(page, pageSize);
        List<Object> objectList = selectByExample();
        PageInfo pageInfo = new PageInfo(objectList);

        //简化返回数据
        Page<Object> pageReturn = new Page<>();
        pageReturn.setTotal((int) pageInfo.getTotal());
        pageReturn.setItems(pageInfo.getList());
        pageReturn.setPageNum(page);
        pageReturn.setPageSize(pageSize);

    }

    /**
     * 模拟数据库查询
     * @return
     */
    public static List<Object> selectByExample() {
        return Lists.newArrayList();
    }
}
