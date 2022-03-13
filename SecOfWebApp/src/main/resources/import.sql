INSERT INTO roles (id, name) VALUES(1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES(2, 'ROLE_ADMIN');

-- init with test and testtest, todo remove me in production (what could possibly go wrong)
INSERT INTO users (password, username,email) VALUES('$2a$10$vpFqQIbEm0Zd0eco2hLxgOTiujxoziOGvKgbX6KE1ud8hwdHJXsE2', 'test','test@test.com');
INSERT INTO users (password, username,email) VALUES('$2a$10$vpFqQIbEm0Zd0eco2hLxgOTiujxoziOGvKgbX6KE1ud8hwdHJXsE2', 'test2','test2@test.com');

INSERT INTO user_roles (user_id, role_id) VALUES(1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES(1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES(2, 1);

