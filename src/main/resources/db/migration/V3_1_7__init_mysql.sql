CREATE TABLE activity
(
    user BIGINT  NOT NULL,
    type TINYINT NOT NULL,
    id   BIGINT  NOT NULL DEFAULT -1,
    uts  BIGINT  NOT NULL,
    PRIMARY KEY (user, type, id)
);

CREATE TABLE game
(
    id   BIGINT        NOT NULL,
    name VARCHAR(2000) NOT NULL,
    icon VARCHAR(256) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE module
(
    guild  BIGINT      NOT NULL,
    module VARCHAR(32) NOT NULL,
    flag   TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (guild, module)
);

CREATE TABLE permission
(
    guild   BIGINT      NOT NULL,
    channel BIGINT      NOT NULL,
    type    TINYINT     NOT NULL,
    id      BIGINT      NOT NULL DEFAULT -1,
    node    VARCHAR(32) NOT NULL,
    flag    TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (guild, channel, type, id, node)
);

CREATE TABLE prefix
(
    guild  BIGINT      NOT NULL,
    prefix VARCHAR(32) NOT NULL DEFAULT '>',
    PRIMARY KEY (guild)
);
