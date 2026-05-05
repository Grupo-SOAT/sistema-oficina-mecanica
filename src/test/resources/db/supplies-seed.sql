DELETE FROM supplies;
ALTER TABLE supplies ALTER COLUMN supply_id RESTART WITH 1;

INSERT INTO supplies (supply_id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (1, 'SKU-001', 'Oleo sintetico', 'Oleo para motor', 49.90, 10, 2, 8, CURRENT_TIMESTAMP);

INSERT INTO supplies (supply_id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (2, 'SKU-002', 'Filtro de oleo', 'Filtro padrao', 25.50, 10, 1, 20, CURRENT_TIMESTAMP);

INSERT INTO supplies (supply_id, sku, name, description, unit_price, supplied_by, reserved_quantity, available_quantity, created_at)
VALUES (3, 'TO-DELETE-001', 'Insumo para excluir', 'Exclusao', 10.00, 10, 0, 0, CURRENT_TIMESTAMP);

ALTER TABLE supplies ALTER COLUMN supply_id RESTART WITH 4;
