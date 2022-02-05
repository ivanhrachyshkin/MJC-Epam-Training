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

    FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE CASCADE,
    UNIQUE (gift_certificate_id, tag_id)
);

create table ORDER
(
    id SERIAL NOT NULL PRIMARY KEY,
    user_id             INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    gift_certificate_id INTEGER NOT NULL REFERENCES gift_certificate (id) ON DELETE CASCADE,
    price               REAL    not NULL,
    date                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UNIQUE (user_id, gift_certificate_id)
)