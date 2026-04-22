-- =========================
-- CLIENT
-- =========================
CREATE TABLE client (
    client_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    document VARCHAR(50),
    document_type VARCHAR(50),
    phone VARCHAR(50),
    email VARCHAR(255)
);

-- =========================
-- VEHICLE
-- =========================
CREATE TABLE vehicle (
    vehicle_id SERIAL PRIMARY KEY,
    license_plate VARCHAR(20),
    brand VARCHAR(100),
    model VARCHAR(100),
    year INT,
    color VARCHAR(50)
);

-- =========================
-- USER
-- =========================
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(100),
    password VARCHAR(255),
    role TEXT[]
);

-- =========================
-- SERVICE ORDER
-- =========================
CREATE TABLE service_order (
    service_order_id SERIAL PRIMARY KEY,
    status VARCHAR(50),

    client_id INT,
    vehicle_id INT,

    description TEXT,
    estimate_amount NUMERIC(10,2),

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    inspected_at TIMESTAMP,
    partially_rejected_at TIMESTAMP,
    rejected_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    approved_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    delivered_at TIMESTAMP,

    created_by INT,
    inspect_by INT,
    completed_by INT,
    delivered_by INT,

    CONSTRAINT fk_so_client FOREIGN KEY (client_id) REFERENCES client(client_id),
    CONSTRAINT fk_so_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),

    CONSTRAINT fk_so_created_by FOREIGN KEY (created_by) REFERENCES "user"(user_id),
    CONSTRAINT fk_so_inspect_by FOREIGN KEY (inspect_by) REFERENCES "user"(user_id),
    CONSTRAINT fk_so_completed_by FOREIGN KEY (completed_by) REFERENCES "user"(user_id),
    CONSTRAINT fk_so_delivered_by FOREIGN KEY (delivered_by) REFERENCES "user"(user_id)
);

-- =========================
-- CATALOG SERVICE
-- =========================
CREATE TABLE catalog_service (
    catalog_service_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    base_price NUMERIC(10,2)
);

-- =========================
-- SERVICE
-- =========================
CREATE TABLE service (
    service_id SERIAL PRIMARY KEY,
    status VARCHAR(50),

    catalog_service_id INT,
    service_order_id INT,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    rejected_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    approved_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,

    CONSTRAINT fk_service_catalog FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_service(catalog_service_id),

    CONSTRAINT fk_service_order FOREIGN KEY (service_order_id)
        REFERENCES service_order(service_order_id)
);

-- =========================
-- SUPPLIER
-- =========================
CREATE TABLE supplier (
    supplier_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    document VARCHAR(50),
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50)
);

-- =========================
-- SUPPLY
-- =========================
CREATE TABLE supply (
    supply_id SERIAL PRIMARY KEY,
    sku VARCHAR(100),
    name VARCHAR(255),
    description TEXT,
    unit_price NUMERIC(10,2),
    supplier_id INT,
    status VARCHAR(50),
    available_quantity INT,

    CONSTRAINT fk_supply_supplier FOREIGN KEY (supplier_id)
        REFERENCES supplier(supplier_id)
);

-- =========================
-- SERVICE SUPPLIES
-- =========================
CREATE TABLE service_supplies (
    service_supplies_id SERIAL PRIMARY KEY,
    catalog_service_id INT,
    supply_id INT,
    supply_amount NUMERIC(10,2),

    CONSTRAINT fk_ss_catalog FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_service(catalog_service_id),

    CONSTRAINT fk_ss_supply FOREIGN KEY (supply_id)
        REFERENCES supply(supply_id)
);

-- =========================
-- SERVICE OUTSIDE SUPPLIES
-- =========================
CREATE TABLE service_outside_supplies (
    service_out_supplies_id SERIAL PRIMARY KEY,
    catalog_service_id INT,
    name VARCHAR(255),
    description TEXT,

    CONSTRAINT fk_sos_catalog FOREIGN KEY (catalog_service_id)
        REFERENCES catalog_service(catalog_service_id)
);

-- =========================
-- PURCHASE ORDER
-- =========================
CREATE TABLE purchase_order (
    purchase_order_id SERIAL PRIMARY KEY,
    supply_id INT,
    quoted_unit_price NUMERIC(10,2),
    requested_quantity INT,

    CONSTRAINT fk_po_supply FOREIGN KEY (supply_id)
        REFERENCES supply(supply_id)
);