drop table if exists likes;
drop table if exists minihome_meta;

create table likes
(
    likes_id    bigint auto_increment primary key,
    minihome_id bigint not null,
    user_id bigint not null,
    created_at  datetime(0) not null,
    updated_at  datetime(0) not null
);

create table minihome_meta
(
    minihome_meta_id bigint auto_increment primary key,
    minihome_id      bigint not null,
    like_count       bigint not null
);
