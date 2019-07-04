-- id for "online" activity should always be -1
UPDATE activity
SET id = -1
WHERE type = 1;

-- add source column to activity table and include in primary key
ALTER TABLE activity
    DROP PRIMARY KEY;
ALTER TABLE activity
    ADD COLUMN source BIGINT NOT NULL DEFAULT -1;
ALTER TABLE activity
    ADD PRIMARY KEY (user, type, id, source);
