CREATE TABLE game
(
    id   BIGINT        NOT NULL,
    name VARCHAR(2000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE module
(
    guild  BIGINT     NOT NULL,
    module VARCHAR(4) NOT NULL,
    flag   TINYINT    NOT NULL,
    PRIMARY KEY (guild, module)
);

CREATE TABLE permission
(
    guild   BIGINT      NOT NULL,
    channel BIGINT      NOT NULL,
    type    TINYINT     NOT NULL,
    id      BIGINT      NOT NULL,
    node    VARCHAR(24) NOT NULL,
    flag    TINYINT     NOT NULL,
    PRIMARY KEY (guild, channel, type, id, node)
);

CREATE TABLE prefix
(
    guild  BIGINT      NOT NULL,
    prefix VARCHAR(24) NOT NULL,
    PRIMARY KEY (guild)
);
