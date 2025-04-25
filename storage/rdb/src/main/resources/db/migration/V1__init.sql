drop table if exists attendance;
drop table if exists follow;
drop table if exists guestbook;
drop table if exists lotto;
drop table if exists trade;
drop table if exists user_item;
drop table if exists users;
drop table if exists minihome;
drop table if exists outbox;

create table attendance
(
    attendance_id bigint auto_increment primary key,
    date          date,
    user_id       bigint not null,
    bonus_coin    int    not null
);

create table follow
(
    follow_id   bigint auto_increment primary key,
    followee_id bigint not null,
    follower_id bigint not null,
    created_at  datetime(0) not null,
    updated_at  datetime(0) not null,
    constraint uq_follow unique (follower_id, followee_id)
);

create table guestbook
(
    guestbook_id bigint auto_increment primary key,
    minihome_id  bigint       not null,
    user_id      bigint       not null,
    content      varchar(255) not null,
    created_at   datetime(0) not null,
    updated_at   datetime(0) not null
);

create table lotto
(
    lotto_id    bigint auto_increment primary key,
    user_id     bigint       not null,
    item_grade  varchar(255) not null,
    used        boolean      not null,
    won         boolean      not null,
    reward_coin int          not null,
    created_at       datetime(0) not null,
    updated_at       datetime(0) not null
);

create table trade
(
    trade_id         bigint auto_increment primary key,
    seller_id        bigint       not null,
    buyer_id         bigint,
    item             varchar(255) not null,
    trade_status     varchar(255) not null,
    transaction_date datetime(0),
    created_at       datetime(0) not null,
    updated_at       datetime(0) not null
);

create table user_item
(
    user_item_id bigint auto_increment primary key,
    item         varchar(255) not null,
    user_id      bigint       not null,
    created_at   datetime(0) not null,
    updated_at   datetime(0) not null
);

create table users
(
    user_id     bigint auto_increment primary key,
    social_type varchar(255) not null,
    login_id    bigint       not null,
    nickname    varchar(255) not null unique,
    coin        int          not null,
    score       int          not null,
    profile     varchar(255) not null,
    backgrounds varchar(255),
    created_at  datetime(0) not null,
    updated_at  datetime(0) not null,
    constraint uq_social_login unique (social_type, login_id)
);

create table minihome
(
    minihome_id       bigint auto_increment primary key,
    user_id           bigint not null,
    total_visitor_cnt int    not null
);

create table outbox
(
    outbox_id bigint auto_increment primary key,
    topic     varchar(255) not null,
    payload   longtext     not null
);
