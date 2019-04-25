CREATE TABLE `user_0` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
);

CREATE TABLE `user_1` (
  `id` int(11) primary key,
  `name` varchar(200) DEFAULT NULL
) ;

CREATE TABLE `order_0` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ;


CREATE TABLE `order_1` (
  `id` varchar(255) primary key,
  `name` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ;
