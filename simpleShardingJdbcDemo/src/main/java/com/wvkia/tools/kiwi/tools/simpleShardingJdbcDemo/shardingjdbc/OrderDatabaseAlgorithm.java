package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.config.DatabaseCSHAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

public class OrderDatabaseAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        return DatabaseCSHAlgorithm.getDatabase(shardingValue.getValue());
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (String value : shardingValue.getValues()) {
            result.add(DatabaseCSHAlgorithm.getDatabase(value));
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        return availableTargetNames;
    }
}
