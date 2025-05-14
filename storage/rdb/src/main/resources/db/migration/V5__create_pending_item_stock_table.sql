drop table if exists pending_item_stock;

create table pending_item_stock
(
    pending_item_stock_id bigint auto_increment primary key,
    item_id               bigint not null,
    trade_id              bigint not null,
    created_at            datetime(0) not null,
    updated_at            datetime(0) not null
);
