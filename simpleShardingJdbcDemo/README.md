设定一个通用的场景

随着业务需求的增长，用户表数据量越来越大，这时候需要采用分库分表来降低单机IO负载，用户user表使用自增主键，然后我们可以
通过id来作为分库和分表的关键字段

默认库：作用在于没有采取分库分表的表格操作，走默认数据库

为做分库分表之前的数据库，通常是作为逻辑表
```sql

/**用户表**/
CREATE TABLE `user` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL,
  `desc` varchar(255) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/**登录记录表，注意id是varchar字符串，并且存在timestamp类型字段**/
CREATE TABLE `login_trace` (
  `id` varchar(255) primary key,
  `login_name` varchar(200) DEFAULT NULL,
  `desc` varchar(255),
  `login_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**订单表，id为varchar字符串型，关联user_id**/
CREATE TABLE `order` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `desc` varchar(255),
  `login_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

我们原数据库总共三张表，暂不考虑login_trace表

我们制定分库分表策略：

对``user``表按照id进行分库，按照id进行分表
对``order``表按照user_id进行分库，按照id进行分表

库策略：

原库分为两个主库，分别为database_0库和database_1库，使用数字结尾方便进行分库计算，其中每个主库各有一个从库，实现
读写分离，即总共四个库，database_0主库、database_0_r_0、database_0_r_1，database_1主库、database_1_r_0、database_1_r_1

在每一个库各执行sql：
```sql
CREATE TABLE `user_0` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL,
  `desc` varchar(255) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_1` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL,
  `desc` varchar(255) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_0` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `desc` varchar(255),
  `login_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_1` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `desc` varchar(255),
  `login_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

