CREATE TABLE categories (
    owner_id      UUID NOT NULL,
    category_id   UUID NOT NULL,
    name          VARCHAR(300) NOT NULL,
    created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(owner_id, category_id)
);

CREATE TABLE links (
    owner_id       UUID NOT NULL,
    category_id    UUID NOT NULL,
    name           VARCHAR(300) NOT NULL,
    url            VARCHAR(200) NOT NULL,
    description    TEXT NULL,
    deadline       TIMESTAMP NOT NULL,
    created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category
      FOREIGN KEY(owner_id, category_id)
	    REFERENCES categories(owner_id, category_id),
    PRIMARY KEY (owner_id, category_id, url)
);