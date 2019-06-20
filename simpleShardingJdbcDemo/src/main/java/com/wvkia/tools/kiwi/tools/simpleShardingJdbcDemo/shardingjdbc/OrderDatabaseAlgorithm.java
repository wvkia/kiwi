package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.config.DatabaseCSHAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * order表分库策略
 */
public class OrderDatabaseAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {
    //数据库数量
    private int databaseSize=2;

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {

        for (String availableTargetName : availableTargetNames) {
            String i = shardingValue.getValue().hashCode() % databaseSize + "";
            if (availableTargetName.endsWith(i)) {
                return availableTargetName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (String value : shardingValue.getValues()) {
            for (String availableTargetName : availableTargetNames) {
                String i = value.hashCode() % databaseSize + "";
                if (availableTargetName.endsWith(i)) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        return availableTargetNames;
    }
}
