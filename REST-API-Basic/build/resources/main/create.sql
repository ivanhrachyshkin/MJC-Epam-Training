DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS gift_cartificate;
DROP TABLE IF EXISTS gift_tag_certificate;

CREATE TABLE tag
(
    id IDENTITY NOT NULL PRIMARY KEY,
    name CHARACTER VARYING(70) NOT NULL,
    UNIQUE (name)
);
INSERT INTO tag (name)
VALUES ('firstTag');
INSERT INTO tag (name)
VALUES ('secondTag');
INSERT INTO tag (name)
VALUES ('thirdTag');

CREATE TABLE gift_certificate
(
    id IDENTITY NOT NULL PRIMARY KEY,
    name             CHARACTER VARYING(70) NOT NULL,
    description      CHARACTER VARYING(70) NOT NULL,
    price            FLOAT                 NOT NULL,
    duration         INTEGER               NOT NULL,
    create_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_update_date timestamp WITHOUT TIME ZONE NOT NULL,
    UNIQUE (name)
);

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('g1', 'd1', 1.0, 1, '2010-01-01 01:01:01.111', '2010-01-01 01:01:01.111');
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('g2', 'd2', 2.0, 2, '2010-01-01 01:01:01.111', '2010-01-01 01:01:01.111');