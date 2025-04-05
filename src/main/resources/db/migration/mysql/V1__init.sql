drop table if exists attendance;
drop table if exists follow;
drop table if exists guestbook;
drop table if exists lotto;
drop table if exists minihome;
drop table if exists trade;
drop table if exists user_item;
drop table if exists users;

create table attendance
(
    bonus_coin    integer not null,
    date          date,
    attendance_id bigint  not null auto_increment,
    user_id       bigint  not null,
    primary key (attendance_id)
);

create table follow
(
    created_at  DATETIME(0) not null,
    follow_id   bigint not null auto_increment,
    followee_id bigint not null,
    follower_id bigint not null,
    updated_at  DATETIME(0) not null,
    primary key (follow_id)
);

create table guestbook
(
    created_at   DATETIME(0) not null,
    guestbook_id bigint       not null auto_increment,
    minihome_id  bigint       not null,
    updated_at   DATETIME(0) not null,
    user_id      bigint       not null,
    content      varchar(255) not null,
    primary key (guestbook_id)
);

create table lotto
(
    reward_coin integer not null,
    used        bit     not null,
    won         bit     not null,
    lotto_id    bigint  not null auto_increment,
    user_id     bigint  not null,
    primary key (lotto_id)
);

create table minihome
(
    total_visitor_cnt integer not null,
    created_at        DATETIME(0) not null,
    minihome_id       bigint  not null auto_increment,
    updated_at        DATETIME(0) not null,
    user_id           bigint  not null,
    primary key (minihome_id)
);

create table trade
(
    buyer_id         bigint,
    created_at       DATETIME(0) not null,
    seller_id        bigint not null,
    trade_id         bigint not null auto_increment,
    transaction_date datetime(6),
    updated_at       DATETIME(0) not null,
    item             enum ('ANGEL_KIRBY','BLACK_CAT','BLUE_HARIBO','BUNNIES','CROISSANT','CUPCAKE','GRAY_ORANGE_CAT','GRAY_WHITE_CAT','GREEN_HARIBO','KITTY_ON_BALLOON','KITTY_WITH_BALLOON','MARIO','MARIO_BLOCK','MARIO_FLOWER','MARIO_STAR','ORANGE_HARIBO','PANCAKE','PINK_DONUT','PINK_HARIBO','PURPLE_BOY','RAINBOW_KIRBY','RED_BOY','RED_HARIBO','STRAWBERRY','YELLOW_CAT','YELLOW_HARIBO'),
    trade_status     enum ('COMPLETED','ON_SALE') not null,
    primary key (trade_id)
);

create table user_item
(
    created_at   DATETIME(0) not null,
    updated_at   DATETIME(0) not null,
    user_id      bigint not null,
    user_item_id bigint not null auto_increment,
    item         enum ('ANGEL_KIRBY','BLACK_CAT','BLUE_HARIBO','BUNNIES','CROISSANT','CUPCAKE','GRAY_ORANGE_CAT','GRAY_WHITE_CAT','GREEN_HARIBO','KITTY_ON_BALLOON','KITTY_WITH_BALLOON','MARIO','MARIO_BLOCK','MARIO_FLOWER','MARIO_STAR','ORANGE_HARIBO','PANCAKE','PINK_DONUT','PINK_HARIBO','PURPLE_BOY','RAINBOW_KIRBY','RED_BOY','RED_HARIBO','STRAWBERRY','YELLOW_CAT','YELLOW_HARIBO') not null,
    primary key (user_item_id)
);

create table users
(
    coin        integer,
    score       integer,
    created_at  DATETIME(0) not null,
    login_id    bigint       not null,
    updated_at  DATETIME(0) not null,
    user_id     bigint       not null auto_increment,
    nickname    varchar(255) not null,
    backgrounds varbinary(255),
    profile     enum ('BEAR','COW','GIRAFFE') not null,
    social_type enum ('GITHUB','KAKAO') not null,
    primary key (user_id)
);

alter table follow
    add constraint uq_follow unique (follower_id, followee_id);

alter table users
    add constraint uq_social_login unique (social_type, login_id);

alter table users
    add constraint uq_nickname unique (nickname);
