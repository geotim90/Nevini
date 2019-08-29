ALTER TABLE osu_beatmap
    ADD COLUMN max_pp DOUBLE DEFAULT NULL
        AFTER max_combo;

ALTER TABLE osu_beatmap
    MODIFY COLUMN max_combo INT DEFAULT NULL;

ALTER TABLE osu_beatmap
    MODIFY COLUMN difficulty_aim DOUBLE DEFAULT NULL;

ALTER TABLE osu_beatmap
    MODIFY COLUMN difficulty_speed DOUBLE DEFAULT NULL;
