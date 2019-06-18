CREATE TABLE auto_role
(
    guild BIGINT      NOT NULL,
    type  VARCHAR(32) NOT NULL,
    id    BIGINT      NOT NULL DEFAULT -1,
    role  BIGINT      NOT NULL,
    PRIMARY KEY (guild, type, id)
);
