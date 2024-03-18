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
VALUES (now(), 1, 1, 'oceanLetter1@gmail.com', '낯선 바다', 'MALE', 'USER', 26, '1999-04-22', 5);
--        (now(), 1, 1, 'kdo0422@nate.com', '낯선 바다', 'MALE', 'USER', 26, '1999-04-22', 5);
--
-- INSERT INTO letter(uuid, letter_type, sender_id, receiver_id, content, create_date, worry_type,
--                    has_replied, is_delete_by_sender, is_stored, reply_content, equal_gender, age_range_start,
--                    age_range_end)
-- VALUES ('1', null, 1, 2, 'gdgd', now(), 'WORK', false, false, false, null, false, 10, 30);

INSERT INTO letter_image(unique_name, origin_name, image_path)
VALUES ('oceanImage.jpeg', 'oceanImage.jpeg',
        'https://d2bunq4des8hir.cloudfront.net/letter/oceanImage.jpeg');
