package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 用户根据id分库
 * 大于20的放到database_1，小于20的放到database_0
 *
 * 解析关键分库字段与数据库之间的关系
 */
public class UserDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {

    @Override
    public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        for (String each : databaseNames) {
            String i =(shardingValue.getValue() < 20 ? 0:1)+"";


            if (each.endsWith(i)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        for (Integer value : shardingValue.getValues()) {
            String i =(value < 20 ? 0:1)+"";
            for (String tableName : databaseNames) {
                if (tableName.endsWith(i)) {
                    result.add(tableName);
                }

            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        Range<Integer> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            String it =(i < 20 ? 0:1)+"";
            for (String each : databaseNames) {
                if (each.endsWith(it)) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
