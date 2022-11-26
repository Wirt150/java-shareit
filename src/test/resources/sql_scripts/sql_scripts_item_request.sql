INSERT INTO users(id, name, email)
VALUES (1, 'test', 'test@test.com');
INSERT INTO request(id, author, description, created)
VALUES (1, 1, 'test', now());