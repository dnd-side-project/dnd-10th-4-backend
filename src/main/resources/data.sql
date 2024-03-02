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
VALUES (now(), 1, 1, 'oceanLetter', '낯선 바다', 'MALE', 'USER', 26, '1999-04-22', 5);

--
-- INSERT INTO letter(uuid, letter_type, sender_id, receiver_id, content, create_date, worry_type,
--                    has_replied, is_delete_by_sender, is_stored, reply_content, equal_gender, age_range_start,
--                    age_range_end)
-- VALUES ('1', null, 2, 3, '김동웅님이 이상훈님께 보내는 첫번째 테스트 메시지 데이터입니다.',
--         now(), 'WORK', false, false, false, null, false, 10, 30),
--
--        ('2', null, 2, 3, '김동웅님이 이상훈님께 보내는 두번째 테스트 메시지 데이터입니다.',
--         now(), 'LOVE', false, false, false, null, true, 20, 30),
--
--        ('3', null, 2, 3, '김동웅님이 이상훈님께 보내는 세번째 테스트 메시지 데이터입니다.',
--         now(), 'FAMILY', false, false, false, null, false, 30, 40),
--
--        ('4', null, 3, 2, '이상훈님이 김동웅님께 보내는 첫번째 테스트 메시지 데이터입니다.',
--         now(), 'ETC', false, false, false, null, false, 10, 20),
--
--        ('5', null, 3, 2, '이상훈님이 김동웅님께 보내는 두번째 테스트 메시지 데이터입니다.',
--         now(), 'RELATIONSHIP', false, false, false, null, true, 10, 20),
--
--        ('6', null, 3, 2, '이상훈님이 김동웅님께 보내는 세번째 테스트 메시지 데이터입니다.',
--         now(), 'LOVE', false, false, false, null, false, 25, 30);

INSERT INTO letter_image(unique_name, origin_name, image_path)
VALUES ('oceanImage.jpeg', 'oceanImage.jpeg',
        'https://letter-img-bucket.s3.ap-northeast-2.amazonaws.com/letter/oceanImage.jpeg');


