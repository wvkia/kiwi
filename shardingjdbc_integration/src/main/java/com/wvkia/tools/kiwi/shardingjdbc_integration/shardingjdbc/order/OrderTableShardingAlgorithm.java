package com.wvkia.tools.kiwi.shardingjdbc_integration.shardingjdbc.order;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 对order表进行分表，使用id（String）进行分表
 * 计算id对hash值，然后对表进行分配策略
 * 当前表格 order_0、order_1
 * 对id取余 id % 2 如果为0 order_0，如果为1 到order_1
 */
public class OrderTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    private int tableSize=2;
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        for (String each : availableTargetNames) {
            int hashCode = shardingValue.getValue().hashCode() & 0x7FFFFFFF;
            if (each.endsWith(hashCode % tableSize + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(availableTargetNames.size());
        for (String value : shardingValue.getValues()) {
            int hashCode = value.hashCode()& 0x7FFFFFFF;
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(hashCode % tableSize + "")) {
                    result.add(tableName);
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
