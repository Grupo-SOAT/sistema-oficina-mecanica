-- Limpar apenas os serviços e dependências, mantendo estrutura base
DELETE
FROM service_needed_supplies
WHERE service_id IN (SELECT id FROM services);
DELETE
FROM services
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
ALTER TABLE services
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE service_orders
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE vehicles
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE owners
    ALTER COLUMN id RESTART WITH 1;
