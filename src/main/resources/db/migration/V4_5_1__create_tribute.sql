CREATE TABLE tribute_member
(
    guild  BIGINT  NOT NULL,
    member BIGINT  NOT NULL,
    its    BIGINT  NOT NULL,
    flag   TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (guild, member)
);

CREATE TABLE tribute_role
(
    guild BIGINT NOT NULL,
    role  BIGINT NOT NULL,
    PRIMARY KEY (guild)
);

CREATE TABLE tribute_timeout
(
    guild   BIGINT NOT NULL,
    timeout BIGINT NOT NULL,
    PRIMARY KEY (guild)
);
