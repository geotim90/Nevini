CREATE TABLE inactivity
(
    guild BIGINT  NOT NULL,
    type  TINYINT NOT NULL,
    id    BIGINT  NOT NULL DEFAULT -1,
    days  INT     NOT NULL,
    PRIMARY KEY (guild, type, id)
);
