INSERT INTO tag (name)
VALUES ('firstTag');
INSERT INTO tag (name)
VALUES ('secondTag');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('g1', 'd1', 1.0, 1, '2010-01-01 01:01:01.111', '2010-01-01 01:01:01.111');
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('g2', 'd2', 2.0, 2, '2020-02-02 02:02:02.222', '2020-02-02 02:02:02.222');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1,1);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1,2);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (2,1);