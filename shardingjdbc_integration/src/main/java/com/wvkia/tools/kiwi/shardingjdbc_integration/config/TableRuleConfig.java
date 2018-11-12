package com.wvkia.tools.kiwi.shardingjdbc_integration.config;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.slave.MasterSlaveLoadBalanceStrategyType;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.collect.Lists;
import com.wvkia.tools.kiwi.shardingjdbc_integration.shardingjdbc.order.OrderDatabaseShardingAlgorithm;
import com.wvkia.tools.kiwi.shardingjdbc_integration.shardingjdbc.order.OrderTableShardingAlgorithm;
import com.wvkia.tools.kiwi.shardingjdbc_integration.shardingjdbc.user.UserDatabaseShardingAlgorithm;
import com.wvkia.tools.kiwi.shardingjdbc_integration.shardingjdbc.user.UserTableShardingAlgorithm;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.wvkia.tools.kiwi.shardingjdbc_integration.mapper",sqlSessionTemplateRef="sqlSessionTemplate")
public class TableRuleConfig {

    /**配置主master database**/
    @Bean(name="database_0_master")
    @ConfigurationProperties("spring.datasource0.master")
    public DataSource database_0_master(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="database_0_slave_0")
    @ConfigurationProperties("spring.datasource0.slave0")
    public DataSource database_0_slave_0(){
        return DataSourceBuilder.create().build();
    }



    @Bean(name="database_1_master")
    @ConfigurationProperties("spring.datasource1.master")
    public DataSource database_1_master(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="database_1_slave_0")
    @ConfigurationProperties("spring.datasource1.slave0")
    public DataSource database_1_slave_0(){
        return DataSourceBuilder.create().build();
    }

    @Bean("dataSourceRule")
    public DataSourceRule dataSourceRule(@Qualifier("database_0_master") DataSource database_0_master,
                                         @Qualifier("database_0_slave_0") DataSource database_0_slave_0,
                                         @Qualifier("database_1_master") DataSource database_1_master,
                                         @Qualifier("database_1_slave_0") DataSource database_1_slave_0) throws SQLException {

        Map<String, DataSource> slave0DataBase = new HashMap<>(1);
        slave0DataBase.put("database_0_slave_0", database_0_slave_0);

        //创建主从数据库链接
        DataSource database0 = MasterSlaveDataSourceFactory.createDataSource("database_0_master","database_0_master", database_0_master, slave0DataBase, MasterSlaveLoadBalanceStrategyType.ROUND_ROBIN);


        Map<String, DataSource> slave1DataBase = new HashMap<>(1);
        slave0DataBase.put("database_1_slave_0", database_1_slave_0);

        //创建主从数据库链接
        DataSource database1 = MasterSlaveDataSourceFactory.createDataSource("database_1_master","database_1_master", database_1_master, slave1DataBase, MasterSlaveLoadBalanceStrategyType.ROUND_ROBIN);


        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("database0", database0);
        dataSourceMap.put("database1", database1);

        /** 配置数据源规则，即将多个数据源交给sharding-jdbc管理，并且可以设置默认的数据源，
         * 当表没有配置分库规则时会使用默认的数据源
         */
        //注册到shardingjdbc的datasource并设置默认数据库
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "database0");

        return dataSourceRule;
    }

    /**
     * 用户表规则配置
     * @param dataSourceRule
     * @return
     */
    @Bean
    public TableRule userTableRule(DataSourceRule dataSourceRule) {
        //逻辑表名 user
        TableRule userTableRule = TableRule.builder("user")
                //实际表名user_0、user_1
                .actualTables(Lists.newArrayList("user_0", "user_1"))
                //使用id作为分表策略字段
                .tableShardingStrategy(new TableShardingStrategy("id", new UserTableShardingAlgorithm()))
                //使用id作为分库策略字段
                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return userTableRule;
    }

    /**
     * order表分库分表配置
     * @param dataSourceRule
     * @return
     */
    @Bean
    public TableRule orderTableRule(DataSourceRule dataSourceRule) {
        TableRule orderTableRule = TableRule.builder("order")
                .actualTables(Lists.newArrayList("order_0", "order_1"))
                .tableShardingStrategy(new TableShardingStrategy("id", new OrderTableShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new OrderDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return orderTableRule;
    }


    /**
     * 构建shardingdatasource
     * 包含隐含含义，Bean注解表示的参数会被自动注入
     * 所以tableRuleList会注入所有的tableRule
     * @param dataSourceRule
     * @param tableRuleList
     *
     *
     * 问题记录
     * 如果不采用dependson注解会导致循环引用问题，可以注释掉dependson看到提示信息
     * 该注解用于声明当前bean依赖于另外一个bean。所依赖的bean会被容器确保在当前bean实例化之前被实例化
     * 加入该注解确保datasouce0和datsource1在datasource之前被实例化
     */
    @Bean("dataSource")
    @DependsOn({"database_0_master","database_0_slave_0","database_1_master","database_1_slave_0"})
    public DataSource dataSource(DataSourceRule dataSourceRule,List<TableRule> tableRuleList) throws SQLException {
        ShardingRule shardingRule = ShardingRule.builder()
                .tableRules(tableRuleList)
                .dataSourceRule(dataSourceRule)
                .build();
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return dataSource;
    }

    /**
     * 需要手动配置事务管理器
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmap/*.xml"));
        return bean.getObject();
    }

    @Bean("sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
