CREATE TABLE osu_score
(
    score_id   BIGINT       NOT NULL,
    beatmap_id INT          NOT NULL,
    mode       INT          NOT NULL,
    mods       INT          NOT NULL,
    user_id    INT          NOT NULL,
    user_name  VARCHAR(256) NOT NULL,
    score      INT          NOT NULL,
    count300   INT          NOT NULL,
    count100   INT          NOT NULL,
    count50    INT          NOT NULL,
    count_miss INT          NOT NULL,
    max_combo  INT          NOT NULL,
    count_katu INT          NOT NULL,
    count_geki INT          NOT NULL,
    perfect    BOOLEAN      NOT NULL,
    date       BIGINT       NOT NULL,
    rank       VARCHAR(2)   NOT NULL,
    pp         DOUBLE       NOT NULL,
    replay_available BOOLEAN      NOT NULL,
    PRIMARY KEY (beatmap_id, mode, mods)
);
