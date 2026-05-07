-- Seed mínimo para cenários de Service Orders (Cucumber)
-- Cria múltiplas ServiceOrders em diferentes estados para suportar testes isolados

DELETE FROM services;
DELETE FROM service_orders;

-- ID 1: PENDING (para testar START_INSPECTION)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at)
VALUES (1, 1, 1, 'Troca de óleo e filtro', 'PENDING', 120.00, CURRENT_TIMESTAMP - INTERVAL '3 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 2: IN_INSPECTION (para testar COMPLETE_INSPECTION)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at, inspected_at)
VALUES (2, 2, 2, 'Revisão preventiva', 'IN_INSPECTION', 250.00, CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 3: AWAITING_APPROVAL (para testar APPROVE e CANCEL via budget)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at)
VALUES (3, 1, 1, 'Troca de bateria', 'AWAITING_APPROVAL', 180.00, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 4: APPROVED (para testar START_SERVICE)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at, approved_at)
VALUES (4, 2, 2, 'Alinhamento', 'APPROVED', 200.00, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 5: IN_PROGRESS (para testar COMPLETE_SERVICE e CANCEL_SERVICE)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at, started_at)
VALUES (5, 1, 1, 'Troca de óleo completa', 'IN_PROGRESS', 150.00, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 6: COMPLETED (para testar DELIVER_VEHICLE)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at, completed_at)
VALUES (6, 2, 2, 'Revisão completa', 'COMPLETED', 300.00, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- ID 7: CANCELLED (para testar DELIVER_VEHICLE de estado CANCELLED)
INSERT INTO service_orders (service_order_id, client_id, vehicle_id, description, status, estimated_amount, created_at, updated_at, cancelled_at)
VALUES (7, 1, 1, 'Revisão cancelada', 'CANCELLED', 100.00, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Services para testes
-- Para ID 4 (APPROVED) - testar START_SERVICE
INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at, approved_at)
VALUES (1, 4, 1, 120.00, 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Para ID 5 (IN_PROGRESS) - testar COMPLETE_SERVICE e CANCEL_SERVICE
INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at, started_at)
VALUES 
  (2, 5, 2, 150.00, 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
  (3, 5, 3, 100.00, 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Para ID 6 (COMPLETED)
INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at, completed_at)
VALUES (4, 6, 1, 300.00, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- Para ID 7 (CANCELLED)
INSERT INTO services (service_id, service_order_id, catalog_service_id, price, status, created_at, updated_at, cancelled_at)
VALUES (5, 7, 1, 100.00, 'CANCELLED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day');

SELECT setval(pg_get_serial_sequence('service_orders', 'service_order_id'), (SELECT COALESCE(MAX(service_order_id), 0) FROM service_orders));
SELECT setval(pg_get_serial_sequence('services', 'service_id'), (SELECT COALESCE(MAX(service_id), 0) FROM services));
