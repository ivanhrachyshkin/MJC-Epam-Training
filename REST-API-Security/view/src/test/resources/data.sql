INSERT INTO tags (name)
VALUES ('tag1');
INSERT INTO tags (name)
VALUES ('tag2');
INSERT INTO tags (name, active)
VALUES ('tag3', false);
INSERT INTO tags (name)
VALUES ('tag4');
INSERT INTO tags (name)
VALUES ('tag5');
INSERT INTO tags (name)
VALUES ('tag6');
INSERT INTO tags (name)
VALUES ('tag7');
INSERT INTO tags (name, active)
VALUES ('tag8', false);
INSERT INTO tags (name, active)
VALUES ('tag10', false);

INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift1', 'd1', 1.0, 1);
INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift2', 'd2', 2.0, 2);
INSERT INTO gift_certificates (name, description, price, duration, active)
VALUES ('gift3', 'd3', 3.0, 3, false);
INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift4', 'd4', 4.0, 4);
INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift5', 'd5', 5.0, 5);
INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift6', 'd6', 6.0, 6);
INSERT INTO gift_certificates (name, description, price, duration)
VALUES ('gift7', 'd7', 7.0, 7);

INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (1, 1);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (2, 1);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (3, 1);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (4, 2);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (5, 2);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (6, 2);
INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id)
VALUES (7, 2);

INSERT INTO users (username, email, password)
VALUES ('username1', 'email1', 'password1');
INSERT INTO users (username, email, password)
VALUES ('username2', 'email2', 'password2');
INSERT INTO users (username, email, password)
VALUES ('username3', 'email3', 'password3');
INSERT INTO users (username, email, password)
VALUES ('username4', 'email4', 'password4');
INSERT INTO users (username, email, password)
VALUES ('username5', 'email5', 'password5');

INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name)
VALUES ('ROLE_USER');
