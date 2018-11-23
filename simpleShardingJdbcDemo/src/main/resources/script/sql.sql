
create database simpleShardingJdbc_database0;
create database simpleShardingJdbc_database1;



/**每个库分别执行
 */
CREATE TABLE `user_0` (
  `id` int(200) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `user_1` (
  `id` int(200) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `other` (
  `id` int(11) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `order_0` (
  `id` varchar(200) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `order_1` (
  `id` varchar(200) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

