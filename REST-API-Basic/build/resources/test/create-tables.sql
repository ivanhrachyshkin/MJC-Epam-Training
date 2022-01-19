DROP TABLE IF EXISTS gift_certificate_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS gift_certificate;

CREATE TABLE tag
(
    id IDENTITY NOT NULL PRIMARY KEY,
    name CHARACTER VARYING(70) NOT NULL,
    UNIQUE (name)
);

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

CREATE TABLE gift_certificate_tag
(
    id IDENTITY NOT NULL PRIMARY KEY,
    gift_certificate_id INTEGER NOT NULL,
    tag_id              INTEGER NOT NULL,
    UNIQUE (gift_certificate_id, tag_id)
);

ALTER TABLE gift_certificate_tag
    ADD FOREIGN KEY (tag_id)
        REFERENCES tag(id);

ALTER TABLE gift_certificate_tag
    ADD FOREIGN KEY (gift_certificate_id)
        REFERENCES gift_certificate(id);
