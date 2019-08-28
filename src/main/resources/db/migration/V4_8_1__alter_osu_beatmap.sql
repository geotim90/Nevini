ALTER TABLE osu_beatmap
    ADD COLUMN max_pp DOUBLE DEFAULT NULL
        AFTER max_combo;

ALTER TABLE osu_beatmap
    MODIFY COLUMN max_combo INT DEFAULT NULL; -- null when mode=3

ALTER TABLE osu_beatmap
    MODIFY COLUMN difficulty_aim DOUBLE DEFAULT NULL; -- null when mode=3

ALTER TABLE osu_beatmap
    MODIFY COLUMN difficulty_speed DOUBLE DEFAULT NULL; -- null when mode=3
