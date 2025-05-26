create index idx_minihome_meta_like_count_minihome_id on minihome_meta(like_count, minihome_id);
create index idx_minihome_created_at_minihome_id on minihome(created_at, minihome_id);
create index idx_minihome_total_visitor_cnt_minihome_id on minihome(total_visitor_cnt, minihome_id);
create index idx_users_score_user_id on users(score, user_id);
