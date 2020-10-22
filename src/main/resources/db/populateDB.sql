DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-10-18 08:30:30', 'завтрак', 1000),
       (100000, '2020-10-18 11:17:30', 'второй завтрак', 600),
       (100000, '2020-10-18 13:00:30', 'обед', 2000),
       (100001, '2020-10-18 08:30:30', 'завтрак', 1500),
       (100001, '2020-10-18 11:17:30', 'второй завтрак', 900),
       (100001, '2020-10-18 13:00:30', 'обед', 2600);




