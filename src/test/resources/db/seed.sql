-- Usuários para testes
DELETE FROM users;
INSERT INTO users (user_id, username, password, role) VALUES (1, 'admin', 'admin', 'ADMIN');
INSERT INTO users (user_id, username, password, role) VALUES (2, 'mecanico', 'mecanico', 'MECHANIC');
INSERT INTO users (user_id, username, password, role) VALUES (3, 'atendente', 'atendente', 'ATTENDANT');
INSERT INTO users (user_id, username, password, role) VALUES (4, 'almoxarife', 'almoxarife', 'STOREKEEPER');
INSERT INTO users (user_id, username, password, role) VALUES (5, 'chatbot', 'chatbot-123', 'CHATBOT');
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 6;
