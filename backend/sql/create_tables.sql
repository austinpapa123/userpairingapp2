-- auto-generated definition
create table user
(
    username      varchar(255)                       null comment 'username',
    id            bigint auto_increment comment 'id'
        primary key,
    user_account  varchar(255)                       not null comment 'user account',
    avatar_url    varchar(1023)                      null comment 'profile photo url',
    gender        tinyint                            null comment 'gender',
    user_password varchar(511)                       not null comment 'user password',
    phone         varchar(127)                       null comment 'phone number',
    email         varchar(511)                       null comment 'email address',
    user_status   int      default 0                 null comment 'user status, e.g. banned',
    create_time   datetime default CURRENT_TIMESTAMP null comment 'create time',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'update time',
    deleted       tinyint  default 0                 not null comment 'if deleted',
    user_role     int      default 0                 not null comment '0 - normal user 1- admin',
    tags          varchar(1024)                      null comment 'json array of tags'
);

