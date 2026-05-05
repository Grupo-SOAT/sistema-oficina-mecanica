-- Limpar dados usados pela feature de ordens de serviço
DELETE
FROM service_needed_supplies
WHERE service_id IN (SELECT id FROM services);
DELETE
FROM services
WHERE id > 0;
DELETE
FROM catalog_services
WHERE id > 0;
DELETE
FROM service_orders
WHERE id > 0;
DELETE
FROM vehicles
WHERE id > 0;
DELETE
FROM owners
WHERE id > 0;

-- Reset sequences
ALTER TABLE owners
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE vehicles
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE catalog_services
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE service_orders
    ALTER COLUMN id RESTART WITH 1;

-- Clientes
INSERT INTO owners (id, document, document_type, name, email, phone, created_at, updated_at)
VALUES (1, '12345678901', 'CPF', 'João Silva', 'joao@email.com', '11987654321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO owners (id, document, document_type, name, email, phone, created_at, updated_at)
VALUES (2, '98765432100', 'CPF', 'Maria Souza', 'maria@email.com', '11999998888', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Veículos
INSERT INTO vehicles (id, owner_id, client_id, license_plate, plate, brand, model, vehicle_year, vin, color, fuel_type,
                      created_at, updated_at)
VALUES (1, 1, 1, 'ABC1234', 'ABC1234', 'Toyota', 'Corolla', 2020, 'VIN123456789', 'Prata', 'GASOLINE',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO vehicles (id, owner_id, client_id, license_plate, plate, brand, model, vehicle_year, vin, color, fuel_type,
                      created_at, updated_at)
VALUES (2, 2, 2, 'DEF5678', 'DEF5678', 'Honda', 'Civic', 2021, 'VIN987654321', 'Preto', 'GASOLINE', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- Serviços de catálogo
INSERT INTO catalog_services (id, name, description, created_at, updated_at)
VALUES (1, 'Troca de Óleo', 'Troca de óleo e filtro do motor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO catalog_services (id, name, description, created_at, updated_at)
VALUES (2, 'Alinhamento', 'Alinhamento de rodas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Ordens de serviço
INSERT INTO service_orders (id, client_id, vehicle_id, description, estimated_amount, status, created_at, updated_at,
                            approved_at)
VALUES (1, 1, 1, 'Troca de óleo e filtro', 150.00, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO service_orders (id, client_id, vehicle_id, description, estimated_amount, status, created_at, updated_at,
                            approved_at, started_at, completed_at)
VALUES (2, 2, 2, 'Alinhamento e revisão', 200.00, 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '2024-01-02 09:00:00'::timestamp,
        '2024-01-02 09:30:00'::timestamp,
        '2024-01-02 11:00:00'::timestamp);

-- Reajustar sequência
ALTER TABLE owners
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE vehicles
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE catalog_services
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE service_orders
    ALTER COLUMN id RESTART WITH 3;
