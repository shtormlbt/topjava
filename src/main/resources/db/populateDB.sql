DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories)
VALUES ('2020-10-18 08:30:30.411', 'завтрак', 1000),
       ('2020-10-18 11:17:30.411', 'второй завтрак', 600),
       ('2020-10-18 13:00:30.411', 'обед', 2000),
       ('2020-10-18 08:30:30.411', 'завтрак', 1500),
       ('2020-10-18 11:17:30.411', 'второй завтрак', 900),
       ('2020-10-18 13:00:30.411', 'обед', 2600);

INSERT INTO user_meal (user_id,meal_id)
VALUES (100000,100002),
       (100000,100003),
       (100000,100004),
       (100001,100005),
       (100001,100006),
       (100001,100007);



