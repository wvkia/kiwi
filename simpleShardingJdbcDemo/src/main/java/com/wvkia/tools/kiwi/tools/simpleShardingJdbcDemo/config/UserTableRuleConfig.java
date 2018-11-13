package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.collect.Lists;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc.UserDatabaseShardingAlgorithm;
import com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.shardingjdbc.UserTableShardingAlgorithm;
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
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.wvkia.tools.kiwi.simpleShardingJdbcDemo.mapper",sqlSessionTemplateRef="sqlSessionTemplate")
public class UserTableRuleConfig {

    /**
     * 配置数据源0，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     * @return
     */
    @Bean(name="database_0")
    @ConfigurationProperties("spring.datasource.database0")
    public DataSource purchase(){
       return DataSourceBuilder.create().build();
    }

    @Bean(name="database_1")
    @ConfigurationProperties("spring.datasource.database1")
    public DataSource manager(){
        return DataSourceBuilder.create().build();
    }


    /**
     * 配置数据源规则，即将多个数据源交给sharding-jdbc管理，并且可以设置默认的数据源，
     * 当表没有配置分库规则时会使用默认的数据源
     * @param database_0
     * @param database_1
     * @return
     */
    @Bean
    public DataSourceRule dataSourceRule(@Qualifier("database_0") DataSource database_0,
                                         @Qualifier("database_1") DataSource database_1){
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        //设置分库映射
        dataSourceMap.put("database_0", database_0);
        dataSourceMap.put("database_1", database_1);
        //设置默认库，两个库以上时必须设置默认库。默认库的数据源名称必须是dataSourceMap的key之一
        return new DataSourceRule(dataSourceMap,"database_0");
    }

    @Bean
    public DatabaseShardingStrategy testDatabaseShardingStrategy() {
        return new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm());
    }

    @Bean
    public TableShardingStrategy testTableShardingStrategy() {
        return new TableShardingStrategy("id", new UserTableShardingAlgorithm());
    }

    @Bean
    public TableRule testTableRule(DataSourceRule dataSourceRule) {
        TableRule testTableRule = TableRule.builder("user")
                .actualTables(Lists.newArrayList("user_0", "user_1"))
                .tableShardingStrategy(new TableShardingStrategy("id", new UserTableShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return testTableRule;
    }

    @Bean
    public ShardingRule shardingRule(DataSourceRule dataSourceRule,TableRule testTableRule) {
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Lists.newArrayList(testTableRule))
                .build();
    }

    /**
     * 问题记录
     * 如果不采用dependson注解会导致循环引用问题，可以注释掉dependson看到提示信息
     * 该注解用于声明当前bean依赖于另外一个bean。所依赖的bean会被容器确保在当前bean实例化之前被实例化
     *
     * 加入该注解确保datasouce0和datsource1在datasource之前被实例化
     * @param shardingRule
     * @return
     * @throws SQLException
     */


    @Bean(name="dataSource")
    @DependsOn({ "database_0", "database_1"})
    @Primary
    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule);
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
