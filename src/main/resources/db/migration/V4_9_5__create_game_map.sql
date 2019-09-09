CREATE TABLE game_name_map
(
    name VARCHAR(2000) NOT NULL,
    id   BIGINT        NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE game_id_map
(
    id    BIGINT  NOT NULL,
    name  VARCHAR(2000)    DEFAULT NULL,
    icon  VARCHAR(256)     DEFAULT NULL,
    multi BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);

INSERT INTO game_id_map (id, name, multi)
VALUES (438122941302046720, 'Xbox Live', true);
