INSERT INTO worry(worry_type)
VALUES ('WORK'),
       ('COURSE'),
       ('RELATIONSHIP'),
       ('BREAK_LOVE'),
       ('LOVE'),
       ('STUDY'),
       ('FAMILY'),
       ('ETC');

INSERT INTO member(create_Date, update_age_count, update_gender_count, email, nick_name, gender, role, age, birth_day,
                   letter_count)
VALUES (now(), 1, 1, 'oceanLetter1@gmail.com', '낯선 바다', 'MALE', 'ADMIN', 26, '1999-04-22', 5);

INSERT INTO letter_image(unique_name, origin_name, image_path)
VALUES ('oceanImage.jpeg', 'oceanImage.jpeg',
        'https://d2bunq4des8hir.cloudfront.net/letter/oceanImage.jpeg');
