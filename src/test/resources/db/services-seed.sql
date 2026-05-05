DELETE FROM service_needed_supplies WHERE service_id IN (SELECT service_id FROM services);
DELETE FROM services;
ALTER TABLE services ALTER COLUMN service_id RESTART WITH 1;

INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (1, 1, 1, 150.00, 'AWAITING_APPROVAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (2, 1, 2, 200.00, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (3, 1, 1, 99.90, 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at)
VALUES (4, 2, 1, 75.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO service_needed_supplies (service_id, id_supply, note, quantity)
VALUES (1, 1, 'Óleo para o motor', 2);

INSERT INTO service_needed_supplies (service_id, id_supply, note, quantity)
VALUES (1, 2, 'Filtro de óleo', 1);

INSERT INTO service_needed_supplies (service_id, id_supply, note, quantity)
VALUES (3, 1, null, 1);

ALTER TABLE services ALTER COLUMN service_id RESTART WITH 5;
