-- Usuários para testes
DELETE FROM users;
INSERT INTO users (id, username, password, role) VALUES (1, 'admin', 'admin', 'ADMIN');
INSERT INTO users (id, username, password, role) VALUES (2, 'mecanico', 'mecanico', 'MECHANIC');
INSERT INTO users (id, username, password, role) VALUES (3, 'atendente', 'atendente', 'ATTENDANT');
INSERT INTO users (id, username, password, role) VALUES (4, 'almoxarife', 'almoxarife', 'STOREKEEPER');
INSERT INTO users (id, username, password, role) VALUES (5, 'chatbot', 'chatbot-123', 'CHATBOT');
ALTER TABLE users ALTER COLUMN id RESTART WITH 6;
