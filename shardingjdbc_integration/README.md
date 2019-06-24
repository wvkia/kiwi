设定一个通用的场景

随着业务需求的增长，用户表数据量越来越大，这时候需要采用分库分表来降低单机IO负载，用户user表使用自增主键，然后我们可以
通过id来作为分库和分表的关键字段

默认库：作用在于没有采取分库分表的表格操作，走默认数据库

为做分库分表之前的数据库，通常是作为逻辑表
```sql

/**用户表**/
CREATE TABLE `user` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/**其他表**/
CREATE TABLE `other` (
  `id` varchar(255) primary key,
  `desc` varchar(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**订单表，id为varchar字符串型，关联user_id**/
CREATE TABLE `order` (
  `id` varchar(255) primary key,
  `user_id` varchar(255) ,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

我们原数据库总共三张表，暂不考虑login_trace表

我们制定分库分表策略：
order 表id取模，也就是 对id取hashcode然后取模
user 表id取模，也就是 对id取hashcode然后取模

库策略：

只考虑读写分离库分表问题，数据库分别为database_r和database_rw
自动同步之后，设置分表策略


配置信息

```java
/**
 * 读写分离模块实现分库分表
 */
@Configuration
@MapperScan(basePackages = "com.wvkia.tools.kiwi.shardingjdbc_integration.mapper",sqlSessionTemplateRef="sqlSessionTemplate")
public class TableRuleConfig {

    //1. 配置主备数据源
    /**配置主master database**/
    @Bean(name="database_0_master")
    @ConfigurationProperties("jdbc.database0.master")
    public DataSource database_0_master(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="database_0_slave_0")
    @ConfigurationProperties("jdbc.database0.slave0")
    public DataSource database_0_slave_0(){
        return DataSourceBuilder.create().build();
    }

    //2. 组装datasourceRule
    @Bean("dataSourceRule")
    public DataSourceRule dataSourceRule(@Qualifier("database_0_master") DataSource database_0_master,
                                         @Qualifier("database_0_slave_0") DataSource database_0_slave_0) throws SQLException {

        Map<String, DataSource> slave0DataBase = new HashMap<>(1);
        slave0DataBase.put("database_0_slave_0", database_0_slave_0);

        //创建主从数据库链接
        DataSource database0 = MasterSlaveDataSourceFactory.createDataSource("database_0_master","database_0_master", database_0_master, slave0DataBase, MasterSlaveLoadBalanceStrategyType.ROUND_ROBIN);

        Map<String, DataSource> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put("database0", database0);

        /** 配置数据源规则，即将多个数据源交给sharding-jdbc管理，并且可以设置默认的数据源，
         * 当表没有配置分库规则时会使用默认的数据源
         */
        //注册到shardingjdbc的datasource并设置默认数据库
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "database0");

        return dataSourceRule;
    }

    //3. 用户分表策略
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
//                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return userTableRule;
    }

    //4. order表分表策略
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
//                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new OrderDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return orderTableRule;
    }


    //5. 组装databaseSource
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
    @DependsOn({"database_0_master","database_0_slave_0"})
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

```