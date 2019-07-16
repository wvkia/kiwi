create table `article`(
    id integer ,
    title varchar(255) ,
    article_type integer ,
    author_id integer ,
    content text,
    create_time date
);

create table `author`(
    id integer ,
    name  varchar (255),
    sex varchar (255)
);

