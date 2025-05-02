drop table if exists notification;
drop table if exists notification_read_marker;

create table notification
(
    notification_id   bigint auto_increment primary key,
    user_id           bigint       not null,
    message           varchar(255) not null,
    notification_type varchar(255) not null,
    created_at        datetime(0) not null,
    updated_at        datetime(0) not null
);

create table notification_read_marker
(
    notification_read_marker_id bigint auto_increment primary key,
    user_id                     bigint not null,
    last_read_notification_id   bigint not null,
    created_at                  datetime(0) not null,
    updated_at                  datetime(0) not null
);
