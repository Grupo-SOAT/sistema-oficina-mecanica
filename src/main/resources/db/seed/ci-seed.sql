-- =========================
-- USERS
-- =========================
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(100),
    password VARCHAR(255),
    role TEXT[]
);

INSERT INTO users (user_id, user_name, password, role) VALUES
(1, 'admin', '$2a$10$IETgyptrfhwJ11oTxUZEf.JAW/km7JpgmN8g51oAp4clPB8LOWCPa', ARRAY['ADMIN'])
