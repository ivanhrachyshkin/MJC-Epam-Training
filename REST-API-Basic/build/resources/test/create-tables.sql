DROP TABLE IF EXISTS gift_certificate_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS gift_certificate;

CREATE TABLE gift_certificate
(
    id IDENTITY NOT NULL PRIMARY KEY,
    name             CHARACTER VARYING(70) NOT NULL,
    description      CHARACTER VARYING(70) NOT NULL,
    price            FLOAT                 NOT NULL,
    duration         INTEGER               NOT NULL,
    create_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UNIQUE (name)
);

CREATE TABLE tag
(
    id IDENTITY NOT NULL PRIMARY KEY,
    name CHARACTER VARYING(70) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE gift_certificate_tag
(
    tag_id              INTEGER NOT NULL,
    gift_certificate_id INTEGER NOT NULL,

    FOREIGN KEY (tag_id) REFERENCES tag(id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate(id),
    UNIQUE (gift_certificate_id, tag_id)
);

