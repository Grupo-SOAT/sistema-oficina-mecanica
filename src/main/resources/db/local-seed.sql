-- Usuários para desenvolvimento local
DELETE FROM users;
INSERT INTO users (user_id, user_name, password, role) VALUES (1, 'admin', '$2a$10$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBaOkA7I0.6', ARRAY['ROLE_ADMIN']);
INSERT INTO users (user_id, user_name, password, role) VALUES (2, 'mecanico', '$2a$10$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBaOkA7I0.6', ARRAY['ROLE_MECHANIC']);
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 3;
