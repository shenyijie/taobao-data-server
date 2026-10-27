# create database data_server;

drop table if exists user_info;
create table user_info
(
    id          int auto_increment primary key,
    account     varchar(20) not null,
    token       varchar(36) not null,
    name        varchar(20) not null,
    phone       varchar(20) null,
    enable      int   default 1,
    type        int   default 0,
    create_time bigint      not null,
    update_time bigint      null,
    discount    int   default 1,
    balance     float default 0,
    remark      varchar(50) null
);

insert into data_server.user_info (id, account, token, name, phone, enable, type, create_time, update_time, discount, balance, remark)
values  (1, 'admin', '46e831699df5ab12cec90d945a412f45', '管理员', null, 1, 0, 1754880772000, 1754880772000, 1, 200, null),
        (2, 'zhao', '6d96cdf3551f41904d93764b6239c9a5', '赵', null, 1, 0, 1754880772000, 1754880772000, 1, 10000, null),
        (10, 'user', '676491be6007616fc5d21d344abc42e8', '测试用户', null, 1, 0, 1754880772000, 1754880772000, 1, 45000, null);