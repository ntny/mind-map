CREATE TABLE links (
    owner_id       UUID NOT NULL,
    name        varchar(300) NOT NULL,
    url         varchar(200) NOT NULL,
    description text NULL,
    deadline    timestamp NOT NULL,
    PRIMARY KEY (owner_id, url)
);