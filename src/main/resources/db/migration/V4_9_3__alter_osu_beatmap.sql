CREATE TABLE osu_beatmapset
(
    beatmapset_id        INT           NOT NULL,
    approved             INT           NOT NULL,
    submit_date          BIGINT DEFAULT NULL,
    approved_date        BIGINT DEFAULT NULL,
    last_update          BIGINT DEFAULT NULL,
    artist               VARCHAR(256)  NOT NULL,
    creator_name         VARCHAR(256)  NOT NULL,
    creator_id           INT           NOT NULL,
    source               VARCHAR(256)  NOT NULL,
    genre                INT           NOT NULL,
    language             INT           NOT NULL,
    title                VARCHAR(256)  NOT NULL,
    tags                 VARCHAR(1000) NOT NULL,
    download_unavailable BOOLEAN       NOT NULL,
    audio_unavailable    BOOLEAN       NOT NULL,
    PRIMARY KEY (beatmapset_id)
);

INSERT INTO osu_beatmapset(beatmapset_id, approved, artist, creator_name, creator_id, source, genre, language, title,
                           tags, download_unavailable, audio_unavailable)
SELECT DISTINCT beatmapset_id,
                approved,
                artist,
                creator_name,
                creator_id,
                source,
                genre,
                language,
                title,
                tags,
                download_unavailable,
                audio_unavailable
FROM osu_beatmap;

ALTER TABLE osu_beatmap
    DROP COLUMN approved;
ALTER TABLE osu_beatmap
    DROP COLUMN submit_date;
ALTER TABLE osu_beatmap
    DROP COLUMN approved_date;
ALTER TABLE osu_beatmap
    DROP COLUMN last_update;
ALTER TABLE osu_beatmap
    DROP COLUMN artist;
ALTER TABLE osu_beatmap
    DROP COLUMN creator_name;
ALTER TABLE osu_beatmap
    DROP COLUMN creator_id;
ALTER TABLE osu_beatmap
    DROP COLUMN source;
ALTER TABLE osu_beatmap
    DROP COLUMN genre;
ALTER TABLE osu_beatmap
    DROP COLUMN language;
ALTER TABLE osu_beatmap
    DROP COLUMN title;
ALTER TABLE osu_beatmap
    DROP COLUMN tags;
ALTER TABLE osu_beatmap
    DROP COLUMN download_unavailable;
ALTER TABLE osu_beatmap
    DROP COLUMN audio_unavailable;

CREATE TABLE osu_beatmap_difficulty
(
    beatmap_id        INT NOT NULL,
    mode              INT NOT NULL,
    mods              INT NOT NULL,
    difficulty_rating DOUBLE DEFAULT NULL,
    difficulty_aim    DOUBLE DEFAULT NULL,
    difficulty_speed  DOUBLE DEFAULT NULL,
    max_combo         INT    DEFAULT NULL,
    max_pp            DOUBLE DEFAULT NULL,
    PRIMARY KEY (beatmap_id, mode, mods)
);

INSERT INTO osu_beatmap_difficulty(beatmap_id, mode, mods, difficulty_rating, difficulty_aim, difficulty_speed,
                                   max_combo, max_pp)
SELECT beatmap_id,
       mode,
       0,
       difficulty_rating,
       difficulty_aim,
       difficulty_speed,
       max_combo,
       max_pp
FROM osu_beatmap;

ALTER TABLE osu_beatmap
    DROP COLUMN difficulty_rating;
ALTER TABLE osu_beatmap
    DROP COLUMN difficulty_aim;
ALTER TABLE osu_beatmap
    DROP COLUMN difficulty_speed;
ALTER TABLE osu_beatmap
    DROP COLUMN max_combo;
ALTER TABLE osu_beatmap
    DROP COLUMN max_pp;

CREATE VIEW osu_beatmap_view AS
SELECT b.beatmap_id,
       b.beatmapset_id,
       b.bpm,
       b.difficulty_size,
       b.difficulty_overall,
       b.difficulty_approach,
       b.difficulty_drain,
       b.hit_length,
       b.total_length,
       b.version,
       b.file_md5,
       b.mode,
       b.favourite_count,
       b.rating,
       b.play_count,
       b.pass_count,
       b.count_normal,
       b.count_slider,
       b.count_spinner,
       s.approved,
       s.submit_date,
       s.approved_date,
       s.last_update,
       s.artist,
       s.creator_name,
       s.creator_id,
       s.source,
       s.genre,
       s.language,
       s.title,
       s.tags,
       s.download_unavailable,
       s.audio_unavailable,
       d.mode AS converted_mode,
       d.mods,
       d.difficulty_rating,
       d.difficulty_aim,
       d.difficulty_speed,
       d.max_combo,
       d.max_pp
FROM osu_beatmap AS b
         JOIN osu_beatmapset AS s ON b.beatmapset_id = s.beatmapset_id
         JOIN osu_beatmap_difficulty AS d ON b.beatmap_id = d.beatmap_id AND b.mode = d.mode AND d.mods = 0;
