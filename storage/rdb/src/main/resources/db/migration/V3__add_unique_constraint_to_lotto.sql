ALTER TABLE lotto ADD CONSTRAINT uq_lotto_user_itemgrade UNIQUE (user_id, item_grade);
