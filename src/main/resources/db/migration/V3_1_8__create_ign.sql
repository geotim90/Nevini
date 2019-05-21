CREATE TABLE ign
(
    guild BIGINT        NOT NULL,
    user  BIGINT        NOT NULL,
    game  BIGINT        NOT NULL,
    name  VARCHAR(2000) NOT NULL,
    PRIMARY KEY (guild, user, game)
);
