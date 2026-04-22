-- Usuários para testes
INSERT INTO users (id, username, password, role) VALUES (1, 'admin', 'admin', 'ADMIN');
INSERT INTO users (id, username, password, role) VALUES (2, 'mecanico', 'mecanico', 'MECHANIC');
INSERT INTO users (id, username, password, role) VALUES (3, 'atendente', 'atendente', 'ATTENDANT');
INSERT INTO users (id, username, password, role) VALUES (4, 'almoxarife', 'almoxarife', 'STOREKEEPER');
INSERT INTO users (id, username, password, role) VALUES (5, 'chatbot', 'chatbot-123', 'CHATBOT');

-- Insumos para testes
INSERT INTO supplies (id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (1, 'SKU-001', 'Oleo sintetico', 'Oleo para motor', 49.90, 10, 2, 8, CURRENT_TIMESTAMP);

INSERT INTO supplies (id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (2, 'SKU-002', 'Filtro de oleo', 'Filtro padrao', 25.50, 10, 1, 20, CURRENT_TIMESTAMP);

INSERT INTO supplies (id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (3, 'TO-DELETE-001', 'Insumo para excluir', 'Exclusao', 10.00, 10, 0, 0, CURRENT_TIMESTAMP);

ALTER TABLE supplies ALTER COLUMN id RESTART WITH 4;
