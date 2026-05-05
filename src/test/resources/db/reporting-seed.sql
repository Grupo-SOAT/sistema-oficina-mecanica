-- Limpar dados existentes
DELETE
FROM service_needed_supplies
WHERE service_id IN (SELECT id FROM services);
DELETE
FROM services;
DELETE
FROM service_orders;
DELETE
FROM vehicles;
DELETE
FROM owners;
DELETE
FROM catalog_services;

-- Reset sequences
ALTER TABLE catalog_services
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE owners
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE vehicles
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE service_orders
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE services
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
INSERT INTO service_orders (id, client_id, vehicle_id, description, estimated_amount, status, created_at, updated_at)
VALUES (1, 1, 1, 'Troca de óleo e filtro', 150.00, 'PENDING',
        '2024-01-01 08:00:00'::timestamp,
        '2024-01-01 08:00:00'::timestamp);

INSERT INTO service_orders (id, client_id, vehicle_id, description, estimated_amount, status, created_at, updated_at,
                            approved_at)
VALUES (2, 2, 2, 'Alinhamento e revisão', 200.00, 'APPROVED',
        '2024-01-02 08:00:00'::timestamp,
        '2024-01-02 08:00:00'::timestamp,
        '2024-01-02 09:00:00'::timestamp);

-- Serviços com timestamps para cálculo de média
INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, started_at, approved_at,
                      completed_at, updated_at)
VALUES (1, 1, 1, 150.00, 'COMPLETED',
        '2024-01-01 08:00:00'::timestamp,
        '2024-01-01 09:00:00'::timestamp,
        '2024-01-01 09:30:00'::timestamp,
        '2024-01-01 11:00:00'::timestamp,
        CURRENT_TIMESTAMP);

INSERT INTO services (id, service_order_id, catalog_service_id, price, status, created_at, started_at, approved_at,
                      completed_at, updated_at)
VALUES (2, 1, 2, 200.00, 'COMPLETED',
        '2024-01-02 08:00:00'::timestamp,
        '2024-01-02 09:00:00'::timestamp,
        '2024-01-02 09:30:00'::timestamp,
        '2024-01-02 10:30:00'::timestamp,
        CURRENT_TIMESTAMP);

-- Reset sequences
ALTER TABLE catalog_services
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE owners
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE vehicles
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE service_orders
    ALTER COLUMN id RESTART WITH 3;
ALTER TABLE services
    ALTER COLUMN id RESTART WITH 3;
