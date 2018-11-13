package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.tools.ConsistentHash;

import java.util.Collection;
import java.util.LinkedHashSet;

public class OrderTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    HashFunction hf = Hashing.md5();

    int numberOfReplicas=100;

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        ConsistentHash<String> consistentHash = new ConsistentHash<String>(hf, numberOfReplicas, availableTargetNames);
        return consistentHash.get(shardingValue.getValue());
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        ConsistentHash<String> consistentHash = new ConsistentHash<String>(hf, numberOfReplicas, availableTargetNames);
        Collection<String> result = new LinkedHashSet<String>(availableTargetNames.size());

        for (String s : shardingValue.getValues()) {
            result.add(consistentHash.get(s));
        }

        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        return availableTargetNames;
    }
}
