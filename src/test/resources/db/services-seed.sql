DELETE FROM service_needed_supplies WHERE service_id IN (SELECT id FROM services);
DELETE FROM services;
ALTER TABLE services ALTER COLUMN id RESTART WITH 1;

INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (1, 1, 1, 150.00, 'AWAITING_APPROVAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (2, 1, 2, 200.00, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (3, 1, 1, 99.90, 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (4, 2, 1, 75.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO service_needed_supplies (service_id, sku, note, quantity)
VALUES (1, 'SKU-001', 'Óleo para o motor', 2);

INSERT INTO service_needed_supplies (service_id, sku, note, quantity)
VALUES (1, 'SKU-002', 'Filtro de óleo', 1);

INSERT INTO service_needed_supplies (service_id, sku, note, quantity)
VALUES (3, 'SKU-001', null, 1);

ALTER TABLE services ALTER COLUMN id RESTART WITH 5;
