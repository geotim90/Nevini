ALTER TABLE game
    MODIFY COLUMN name VARCHAR(1000) NOT NULL;

ALTER TABLE game_id_map
    MODIFY COLUMN name VARCHAR(1000) DEFAULT NULL;

ALTER TABLE game_name_map
    MODIFY COLUMN name VARCHAR(1000) NOT NULL;
