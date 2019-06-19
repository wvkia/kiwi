package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.tools;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wukai
 * @date 2019/4/25
 */
public class MigrationScript {
    public static void main(String[] args) {

        ConsistentHash newcons = getNewConsistent();
        ConsistentHash oldcons = getOldConsisten();

        List<String> newtables = Lists.newArrayList();
        for (int i = 0; i < newtables.size(); i++) {
            String tableold = newtables.get(i);

            //获取表格所有数据
            List<String> allIds = getAllIdsByOldTable(tableold);

            for (String id : allIds) {
                //如果数据hash计算不一致
                if (newcons.Hash(id) != oldcons.Hash(id)) {
                    //旧表查出数据新增到新表数据
                    insert(id, oldcons.Hash(id), newcons.Hash(id));

                    //删除旧表数据
                    delete(id, oldcons.Hash(id));
                }
            }
        }
    }

    /**
     * 获取新的一致性hash
     * @return
     */
    private static ConsistentHash getNewConsistent() {
        return null;
    }

    /**
     * 获取旧的一致性hash，用于计算旧表
     * @return
     */
    private static ConsistentHash getOldConsisten() {
        return null;
    }

    /**
     * 获取旧表所有数据，用于重新插入数据
     * @param oldTable
     * @return
     */
    private static List<String> getAllIdsByOldTable(String oldTable) {
        return null;
    }

    /**
     * 插入旧的数据到新表
     * @param id
     * @param oldTableName
     * @param newTableName
     */
    private static void insert(String id,Integer oldTableName,Integer newTableName) {

    }

    /**
     * 从旧表删除数据
     * @param id
     * @param oldTableName
     */
    private static void delete(String id,Integer oldTableName) {

    }
}
