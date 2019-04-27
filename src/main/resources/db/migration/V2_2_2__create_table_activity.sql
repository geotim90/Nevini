CREATE TABLE activity
(
    user BIGINT  NOT NULL,
    type TINYINT NOT NULL,
    id   BIGINT  NOT NULL,
    uts  BIGINT  NOT NULL,
    PRIMARY KEY (user, type, id)
);
