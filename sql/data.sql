

INSERT INTO users (id, name, password) VALUES (1, 'foo', '$2a$10$sUXJkDdYXnzfBB16Phpytu7y0LGE7lBdd53SKpfIF7bRnAhp1eNoy');

INSERT INTO users_roles (userId, roleName) VALUES (1, 'CanEdit');

INSERT INTO tags (value) VALUES ('Music');
INSERT INTO tags (value) VALUES ('Programming');
INSERT INTO tags (value) VALUES ('Art');

INSERT INTO posts (date, title, authorId, shortlink, value) VALUES (round(extract(epoch from now()) * 1000), 'foo', 1, 'foo-bar',  '42 foo bars');


COMMIT;

ANALYZE;
