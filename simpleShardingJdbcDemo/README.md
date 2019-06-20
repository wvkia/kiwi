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

我们原数据库总共三张表，other

我们制定分库分表策略：

对``user``表按照id进行分库，按照id进行分表
对``order``表按照user_id进行分库，按照id进行分表

库策略：

原库分为两个主库，分别为database_0库和database_1库，使用数字结尾方便进行分库计算
在每一个库各执行sql：
```sql
CREATE TABLE `user_0` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_1` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_0` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_1` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```
进行数据的初始化

二、进行分库分表策略配置

现对user表和order进行分库分表策略设置。
对user表进行id进行分库，策略是 id % 库的size；分表算法是 id / 库的size % 表的size

对order分库算法是user_id % 库size，id % 表size

user分表实现SingleKeyDatabaseShardingAlgorithm接口
```java

public class UserDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {

    private int databaseSize=2;
    
    //处理 键 == 
    @Override
    public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        for (String each : databaseNames) {
            String i =(shardingValue.getValue() % databaseSize)+"";

            //如果是以对应的编号结尾
            if (each.endsWith(i)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    //处理 键  in 
    @Override
    public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        for (Integer value : shardingValue.getValues()) {
            String i =(value % databaseSize)+"";
            for (String tableName : databaseNames) {
                if (tableName.endsWith(i)) {
                    result.add(tableName);
                }

            }
        }
        return result;
    }


    //处理键 between
    @Override
    public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        Range<Integer> range = shardingValue.getValueRange();
        //获取bwteen 键值的序列
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            String it =(i % databaseSize)+"";
            for (String each : databaseNames) {
                if (each.endsWith(it)) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}


```
user分表算法
```java
public class UserTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {
    private int tableSize=2;
    private int databaseSize=2;
    /**
     * sql 中 = 操作时，table的映射
     */
    @Override
    public String doEqualSharding(Collection<String> tableNames, ShardingValue<Integer> shardingValue) {
        for (String each : tableNames) {
            if (each.endsWith(shardingValue.getValue() /databaseSize  % tableSize + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * sql 中 in 操作时，table的映射
     */
    @Override
    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(tableNames.size());
        for (Integer value : shardingValue.getValues()) {
            for (String tableName : tableNames) {
                if (tableName.endsWith(value  /databaseSize  % tableSize + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    /**
     * sql 中 between 操作时，table的映射
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> tableNames,
                                                ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(tableNames.size());
        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : tableNames) {
                if (each.endsWith(i  /databaseSize  % tableSize + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}

```

对order表的分库策略

```java

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
```
分表策略

```java
public class OrderTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    private static int tableSize = 2;
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        for (String availableTargetName : availableTargetNames) {
            String i = shardingValue.getValue().hashCode() % tableSize + "";
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
                String i = value.hashCode() % tableSize + "";
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

```

三、配置信息

```java
/**
 * 分库分表配置
 */
@Configuration
//mybatis扫描
@MapperScan(basePackages = "com.wvkia.tools.kiwi.simpleShardingJdbcDemo.mapper",sqlSessionTemplateRef="sqlSessionTemplate")
public class UserTableRuleConfig {

    //1。 配置数据源 
    /**
     * 配置数据源0，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     * @return
     */
    @Bean(name="database_0")
    @ConfigurationProperties("spring.datasource.simpleshardingjdbcdatabase0")
    public DataSource database_0(){
       return DataSourceBuilder.create().build();
    }

    @Bean(name="database_1")
    @ConfigurationProperties("spring.datasource.simpleshardingjdbcdatabase1")
    public DataSource database_1(){
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

    //2。配置数据库以及表格 分配策略算法
    
    @Bean
    public DatabaseShardingStrategy testDatabaseShardingStrategy() {
        return new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm());
    }

    @Bean
    public TableShardingStrategy testTableShardingStrategy() {
        return new TableShardingStrategy("id", new UserTableShardingAlgorithm());
    }

    @Bean
    public TableRule userTableRule(DataSourceRule dataSourceRule) {
        TableRule userTableRule = TableRule.builder("user")
                .actualTables(Lists.newArrayList("user_0", "user_1"))
                .tableShardingStrategy(new TableShardingStrategy("id", new UserTableShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new UserDatabaseShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return userTableRule;
    }

    @Bean
    public TableRule orderTableRule(DataSourceRule dataSourceRule) {
        TableRule orderTableRule = TableRule.builder("order")
                .actualTables(Lists.newArrayList("order_0", "order_1"))
                .tableShardingStrategy(new TableShardingStrategy("id", new OrderTableShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new OrderDatabaseAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        return orderTableRule;
    }

    //3。分库策略和分表策略整合
    /**
     * 配置分配策略
     * @param dataSourceRule 分库策略
     * @param tableRules 分表策略
     * @return
     */
    @Bean
    public ShardingRule shardingRule(DataSourceRule dataSourceRule, List<TableRule>tableRules) {
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(tableRules)
                .build();
    }

    /**
     * 问题记录
     * 如果不采用dependson注解会导致循环引用问题，可以注释掉dependson看到提示信息
     * 该注解用于声明当前bean依赖于另外一个bean。所依赖的bean会被容器确保在当前bean实例化之前被实例化
     *
     * 加入该注解确保datasouce0和datsource1在datasource之前被实例化
     * @param shardingRule 通过sharding分配策略，构建datasource
     * @return
     * @throws SQLException
     */

    //4。构造myabtis操作的datasourceBean，委托给sharidngjdbc的datasource
    @Bean(name="dataSource")
    @DependsOn({ "database_0", "database_1"})
    @Primary
    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }

    //5。配置事务管理器
    /**
     * 需要手动配置事务管理器
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    //6。配置mybatis的sqlSessionFactory
    /**
     * 根据datasource配置mybatis的sqlsessionfactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmap/*.xml"));
        return bean.getObject();
    }

    //7。配置sqlSessionTemplate
    /**
     * 配置sqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean("sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}


```


四、测试

