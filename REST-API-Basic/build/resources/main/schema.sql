GRANT
ALL
PRIVILEGES
ON
DATABASE
provider TO provider;

ALTER
DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO provider;

DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS gift_certificate;
DROP TABLE IF EXISTS gift_certificate_tag;

CREATE TABLE tag
(
    id   SERIAL                 NOT NULL PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    UNIQUE (name)
);


CREATE TABLE gift_certificate
(
    id               SERIAL PRIMARY KEY,
    name             CHARACTER VARYING(70) NOT NULL,
    description      CHARACTER VARYING(70) NOT NULL,
    price            REAL                  NOT NULL,
    duration         INTEGER               NOT NULL,
    create_date      TIMESTAMP             NOT NULL,
    last_update_date TIMESTAMP             NOT NULL,
    UNIQUE (name)
);

CREATE TABLE gift_certificate_tag
(
    gift_certificate_id INTEGER NOT NULL REFERENCES gift_certificate (id),
    tag_id              INTEGER NOT NULL REFERENCES tag (id),
    UNIQUE (gift_certificate_id, tag_id)
);

GRANT
USAGE,
SELECT
ON ALL SEQUENCES IN SCHEMA public TO provider;

GRANT ALL PRIVILEGES ON TABLE tag TO certificate;
GRANT ALL PRIVILEGES ON TABLE gift_certificate_tag TO certificate;
GRANT ALL PRIVILEGES ON TABLE gift_certificate TO certificate;