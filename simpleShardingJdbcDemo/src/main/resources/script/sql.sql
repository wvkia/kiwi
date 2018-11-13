
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



/**database_0执行语句**/
USE database_0;
INSERT INTO `user_0` (`id`, `name`)
VALUES
	(0,'name0'),
	(2,'name2'),
	(4,'name4'),
	(6,'name6'),
	(8,'name8');
INSERT INTO `user_1` (`id`, `name`)
VALUES
	(1,'name1'),
	(3,'name3'),
	(5,'name5'),
	(7,'name7'),
	(9,'name9'),
	(11,'name11');
INSERT INTO `other` (`id`, `name`)
VALUES
	(1,'a1'),
	(2,'a2'),
	(3,'a3'),
	(4,'a4');


/**database_1执行语句**/
USE database_1;
INSERT INTO `user_0` (`id`, `name`)
VALUES
	(20,'name测试'),
	(22,'name测试'),
	(24,'name测试'),
	(26,'name测试'),
	(28,'name测试');
INSERT INTO `user_1` (`id`, `name`)
VALUES
	(21,'name测试'),
	(23,'name测试'),
	(25,'name测试'),
	(27,'name测试');