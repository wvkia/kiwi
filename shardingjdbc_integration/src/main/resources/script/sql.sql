
create database simpleShardingJdbc_database0;
create database simpleShardingJdbc_database1;


/**
默认库是simpleShardingJdbc_database0
 */
use simpleShardingJdbc_database0;
drop table  IF EXISTS user_0;
CREATE TABLE `user_0` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS user_1;

CREATE TABLE `user_1` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS order_0;

CREATE TABLE `order_0` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS order_1;

CREATE TABLE `order_1` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



drop table  IF EXISTS other;

CREATE TABLE `other` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






use simpleShardingJdbc_database1;
drop table  IF EXISTS user_0;
CREATE TABLE `user_0` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS user_1;

CREATE TABLE `user_1` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS order_0;

CREATE TABLE `order_0` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  IF EXISTS order_1;

CREATE TABLE `order_1` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



drop table  IF EXISTS other;

CREATE TABLE `other` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

