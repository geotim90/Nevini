CREATE TABLE legacy_contribution
(
    guild BIGINT  NOT NULL,
    user  BIGINT  NOT NULL,
    flag  TINYINT NOT NULL,
    PRIMARY KEY (guild, user)
);

CREATE TABLE legacy_timeout
(
    guild BIGINT  NOT NULL,
    type  TINYINT NOT NULL,
    id    BIGINT  NOT NULL,
    value BIGINT  NOT NULL,
    PRIMARY KEY (guild, type, id)
);

CREATE TABLE legacy_activity
(
    guild BIGINT  NOT NULL,
    user  BIGINT  NOT NULL,
    type  TINYINT NOT NULL,
    id    BIGINT  NOT NULL,
    uts   BIGINT  NOT NULL,
    PRIMARY KEY (guild, user, type, id)
);
