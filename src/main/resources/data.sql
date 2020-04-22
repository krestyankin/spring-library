INSERT INTO acl_class (id, class, class_id_type) VALUES
(1, 'ru.krestyankin.library.models.Book','java.lang.String');

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'admin'),
(2, 0, 'ROLE_ADMIN'),
(3, 0, 'ROLE_ADULT'),
(4, 0, 'ROLE_READER');
