-- =====================================================
-- OWNERS
-- =====================================================
CREATE TABLE IF NOT EXISTS owners (
    owner_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    document VARCHAR(50) NOT NULL UNIQUE,
    document_type VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- =====================================================
-- VEHICLES
-- =====================================================
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id SERIAL PRIMARY KEY,

    owner_id BIGINT NOT NULL,

    license_plate VARCHAR(20) NOT NULL UNIQUE,

    brand VARCHAR(100) NOT NULL,

    model VARCHAR(100) NOT NULL,

    year INT NOT NULL,

    color VARCHAR(50) NOT NULL,

    CONSTRAINT fk_vehicle_owner
        FOREIGN KEY (owner_id)
        REFERENCES owners(owner_id)
);

-- =====================================================
-- USERS
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,

    user_name VARCHAR(100) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    role TEXT[] NOT NULL
);

-- =====================================================
-- SERVICE ORDERS
-- =====================================================
CREATE TABLE IF NOT EXISTS service_orders (
    service_order_id SERIAL PRIMARY KEY,

    status VARCHAR(50) NOT NULL,

    owner_id BIGINT NOT NULL,

    vehicle_id BIGINT NOT NULL,

    description TEXT NOT NULL,

    estimate_amount NUMERIC(10,2),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    inspected_at TIMESTAMP,

    partially_rejected_at TIMESTAMP,

    rejected_at TIMESTAMP,

    cancelled_at TIMESTAMP,

    approved_at TIMESTAMP,

    started_at TIMESTAMP,

    completed_at TIMESTAMP,

    delivered_at TIMESTAMP,

    created_by BIGINT,

    inspect_by BIGINT,

    completed_by BIGINT,

    delivered_by BIGINT,

    CONSTRAINT fk_so_owner
        FOREIGN KEY (owner_id)
        REFERENCES owners(owner_id),

    CONSTRAINT fk_so_vehicle
        FOREIGN KEY (vehicle_id)
        REFERENCES vehicles(vehicle_id),

    CONSTRAINT fk_so_created_by
        FOREIGN KEY (created_by)
        REFERENCES users(user_id),

    CONSTRAINT fk_so_inspect_by
        FOREIGN KEY (inspect_by)
        REFERENCES users(user_id),

    CONSTRAINT fk_so_completed_by
        FOREIGN KEY (completed_by)
        REFERENCES users(user_id),

    CONSTRAINT fk_so_delivered_by
        FOREIGN KEY (delivered_by)
        REFERENCES users(user_id)
);

-- =====================================================
-- CATALOG SERVICES
-- =====================================================
CREATE TABLE IF NOT EXISTS catalog_services (
    catalog_service_id SERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    description TEXT NOT NULL,

    base_price NUMERIC(10,2) NOT NULL
);

-- =====================================================
-- SERVICES
-- =====================================================
CREATE TABLE IF NOT EXISTS services (
    service_id SERIAL PRIMARY KEY,

    status VARCHAR(50) NOT NULL,

    catalog_service_id BIGINT NOT NULL,

    service_order_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    rejected_at TIMESTAMP,

    cancelled_at TIMESTAMP,

    approved_at TIMESTAMP,

    started_at TIMESTAMP,

    completed_at TIMESTAMP,

    CONSTRAINT fk_service_catalog
        FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_services(catalog_service_id),

    CONSTRAINT fk_service_order
        FOREIGN KEY (service_order_id)
        REFERENCES service_orders(service_order_id)
);

-- =====================================================
-- SUPPLIERS
-- =====================================================
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id SERIAL PRIMARY KEY,

    name VARCHAR(255),

    document VARCHAR(50),

    contact_person VARCHAR(255),

    email VARCHAR(255),

    phone VARCHAR(50)
);

-- =====================================================
-- SUPPLIES
-- =====================================================
CREATE TABLE IF NOT EXISTS supplies (
    supply_id SERIAL PRIMARY KEY,

    sku VARCHAR(100) NOT NULL UNIQUE,

    name VARCHAR(255) NOT NULL,

    description TEXT NOT NULL,

    unit_price NUMERIC(10,2) NOT NULL,

    supplied_by BIGINT,

    reserved_quantity INT NOT NULL,

    available_quantity INT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_supply_supplier
        FOREIGN KEY (supplied_by)
        REFERENCES suppliers(supplier_id)
);

-- =====================================================
-- SERVICE SUPPLIES
-- =====================================================
CREATE TABLE IF NOT EXISTS service_supplies (
    service_supplies_id SERIAL PRIMARY KEY,

    catalog_service_id BIGINT NOT NULL,

    supply_id BIGINT NOT NULL,

    supply_amount INT NOT NULL,

    CONSTRAINT fk_ss_catalog
        FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_services(catalog_service_id),

    CONSTRAINT fk_ss_supply
        FOREIGN KEY (supply_id)
        REFERENCES supplies(supply_id)
);

-- =====================================================
-- SERVICE OUTSIDE SUPPLIES
-- =====================================================
CREATE TABLE IF NOT EXISTS service_outside_supplies (
    service_out_supplies_id SERIAL PRIMARY KEY,

    catalog_service_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,

    description TEXT NOT NULL,

    CONSTRAINT fk_sos_catalog
        FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_services(catalog_service_id)
);

-- =====================================================
-- PURCHASE ORDERS
-- =====================================================
CREATE TABLE IF NOT EXISTS purchase_orders (
    purchase_order_id SERIAL PRIMARY KEY,

    supply_id BIGINT NOT NULL,

    status VARCHAR(50) NOT NULL,

    estimated_amount NUMERIC(10,2),

    quoted_unit_price NUMERIC(10,2),

    requested_quantity INT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    confirmed_at TIMESTAMP,

    cancelled_at TIMESTAMP,

    dispatched_at TIMESTAMP,

    completed_at TIMESTAMP,

    CONSTRAINT fk_po_supply
        FOREIGN KEY (supply_id)
        REFERENCES supplies(supply_id)
);