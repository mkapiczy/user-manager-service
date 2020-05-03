create table address
(
    id      bigint not null auto_increment primary key,
    street  varchar(32),
    zip_code varchar(16),
    city    varchar(256),
    country varchar(256)
);


create table user
(
    id         bigint not null auto_increment primary key,
    name       varchar(256),
    email      varchar(64),
    password   varchar(64),
    address_id bigint,
    FOREIGN KEY (address_id) REFERENCES address (id)
);

