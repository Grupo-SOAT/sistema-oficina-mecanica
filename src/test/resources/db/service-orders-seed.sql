-- Seed mínimo para cenários de Service Orders (Cucumber)
-- Mantém dados necessários para os cenários sem depender da ordem de execução

DELETE FROM services;
DELETE FROM service_orders;

INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at)
VALUES
  (1, 1, 1, 'Troca de óleo e filtro', 'PENDING', 120.00, CURRENT_TIMESTAMP - INTERVAL '3 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
  (2, 2, 2, 'Revisão preventiva', 'APPROVED', 250.00, CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at, approved_at, started_at, completed_at)
VALUES
  (1, 1, 1, 120.00, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '3 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '3 day', CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
  (2, 1, 2, 250.00, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP);

SELECT setval(pg_get_serial_sequence('service_orders', 'service_order_id'), (SELECT COALESCE(MAX(service_order_id), 0) FROM service_orders));
SELECT setval(pg_get_serial_sequence('services', 'service_id'), (SELECT COALESCE(MAX(service_id), 0) FROM services));
