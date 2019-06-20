package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.tools.ConsistentHash;

import java.util.Collection;
import java.util.LinkedHashSet;

public class OrderTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    private static int tableSize = 2;
    private static int databaseSize=2;
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        for (String availableTargetName : availableTargetNames) {
            String i = shardingValue.getValue().hashCode() /databaseSize  % tableSize + "";
            if (availableTargetName.endsWith(i)) {
                return availableTargetName;
            }
        }
        throw new IllegalArgumentException();

    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>();
        for (String value : shardingValue.getValues()) {
            for (String availableTargetName : availableTargetNames) {
                String i = value.hashCode() /databaseSize % tableSize + "";
                if (availableTargetName.endsWith(i)) {
                    result.add(availableTargetName);
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
