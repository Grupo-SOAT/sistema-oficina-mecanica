-- Limpar apenas os serviços e dependências, mantendo estrutura base
DELETE
FROM service_needed_supplies
WHERE service_id IN (SELECT service_id FROM services);
DELETE
FROM services
WHERE service_id > 0;
DELETE
FROM service_orders
WHERE service_order_id > 0;
DELETE
FROM vehicles
WHERE vehicle_id > 0;
DELETE
FROM owners
WHERE owner_id > 0;

-- Reset sequences
ALTER TABLE services
    ALTER COLUMN service_id RESTART WITH 1;
ALTER TABLE service_orders
    ALTER COLUMN service_order_id RESTART WITH 1;
ALTER TABLE vehicles
    ALTER COLUMN vehicle_id RESTART WITH 1;
ALTER TABLE owners
    ALTER COLUMN owner_id RESTART WITH 1;
