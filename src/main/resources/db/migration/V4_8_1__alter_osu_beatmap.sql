ALTER TABLE osu_beatmap
    ADD COLUMN max_pp DOUBLE DEFAULT NULL
        AFTER max_combo;
