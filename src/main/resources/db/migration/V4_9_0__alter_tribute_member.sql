ALTER TABLE tribute_member
    ADD COLUMN delay BIGINT DEFAULT NULL
        AFTER start;
