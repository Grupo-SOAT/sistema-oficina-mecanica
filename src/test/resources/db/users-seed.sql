-- Limpa tudo
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Inserções com ID controlado (IMPORTANTE)
INSERT INTO users (user_id, user_name, password, role) VALUES
-- ID 1 (usado em vários testes)
(1, 'admin', '$2y$10$ctCsqKe9zwz1AOIQtY0tWOFGGINVPB8Vr/7Jd.UMYaFndnGAxMfvW', ARRAY['ADMIN']),

-- ID 2 (usado em update/delete)
(2, 'mecanico', '$2y$10$e0F/GWAqsSUaUcJD2aWElewev1O98QK6f4s4ZdHbYOvCWiRkEwxYe', ARRAY['MECHANIC']),

-- Outros usuários do login
(3, 'atendente', '$2y$10$xvI8qbW.peeyaFztje20mu0vnEK9Yit771593h7XW0m5hygmNR4y2', ARRAY['ATTENDANT']),
(4, 'almoxarife', '$2y$10$iPibt68XR9.kWzjcTyGxAuoJhGOdNcX4Dlbue.Waskf2xmcVl0zRC', ARRAY['STOREKEEPER']),

-- Usuário para teste de senha inválida
(5, 'valido', '$2y$10$HHga6ioVH0h7SBCqGsZsE.QHkcB.uwupnd3vB4rIqL5jHsOMWPI8.', ARRAY['ADMIN']),

-- Usuários extras (melhoram paginação e robustez)
(6, 'user1', '$2y$10$rfMRrozYopduPCLLeQam8OHB0GJkWIzIbP8pQ4xtd4JOx.LCs7yQy', ARRAY['CHATBOT']),
(7, 'user2', '$2y$10$rfMRrozYopduPCLLeQam8OHB0GJkWIzIbP8pQ4xtd4JOx.LCs7yQy', ARRAY['ATTENDANT']),
(8, 'user3', '$2y$10$rfMRrozYopduPCLLeQam8OHB0GJkWIzIbP8pQ4xtd4JOx.LCs7yQy', ARRAY['STOREKEEPER']);
