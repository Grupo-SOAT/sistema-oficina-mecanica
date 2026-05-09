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
(1, 'admin', '$2a$10$IETgyptrfhwJ11oTxUZEf.JAW/km7JpgmN8g51oAp4clPB8LOWCPa', ARRAY['ADMIN']),
(2, 'mecanico', '$2a$10$zF9DASX8Vrl.9RAtkZ6kkOyajVXfMtJ1ubNMMHTYM4wYBJhkMs/RK', ARRAY['MECHANIC']),
(3, 'atendente', '$2a$10$CuqGHQMfvzb312mnKrrkPOCBeIeXyr75O6j98Ll.CFuCHwSyAYrC6', ARRAY['ATTENDANT']),
(4, 'almoxarife', '$2a$10$UqbvRK7u7k1rwuPX87WIKOXyK1Wo2bNoYQf9PRdlE//eIt.FFCrei', ARRAY['STOREKEEPER']),
(5, 'chatbot', '$2a$10$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBaOkA7I0.6', ARRAY['CHATBOT']);
