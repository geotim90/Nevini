CREATE TABLE feed
(
    guild   BIGINT      NOT NULL,
    type    VARCHAR(32) NOT NULL,
    channel BIGINT      NOT NULL,
    uts     BIGINT      NOT NULL,
    PRIMARY KEY (guild, type)
);
