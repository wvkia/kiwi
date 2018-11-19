package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.config;

import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.tools.ConsistentHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @dependson("datasource")的原因是datasourceRule需要在database初始化之后进行初始化
 */
@Component
@DependsOn({"dataSource"})
public class DatabaseCSHAlgorithm  implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    private DataSourceRule dataSourceRule;

    private static ConsistentHash<String> consistentHash ;

    HashFunction hf = Hashing.md5();

    int numberOfReplicas=100;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Collection<String> collection = dataSourceRule.getDataSourceNames();
        consistentHash = new ConsistentHash(hf, numberOfReplicas, collection);
    }

    public static String getDatabase(Object key) {
        return consistentHash.get(key);

    }
}
