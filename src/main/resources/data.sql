INSERT INTO worry(worry_type)
VALUES ('WORK'),
       ('COURSE'),
       ('RELATIONSHIP'),
       ('BREAK_LOVE'),
       ('LOVE'),
       ('STUDY'),
       ('FAMILY'),
       ('ETC');

INSERT INTO MEMBER(create_Date, update_age_count, update_gender_count, email, nick_name, gender, role, age)
VALUES (now(), 0, 0, 'kdo0422@nate.com', '낯선 유저1', 'MALE', 'USER', 26),
       (now(), 0, 0, 'dog@naver.com', '낯선 강아지', 'MALE', 'USER', 20),
       (now(), 0, 0, 'cat@naver.com', '낯선 고양이', 'MALE', 'USER', 20),
       (now(), 0, 0, 'pig@naver.com', '낯선 돼지', 'MALE', 'USER', 100),
       (now(), 0, 0, 'cow@naver.com', '낯선 소', 'MALE', 'USER', 30),
       (now(), 0, 0, 'chicken@naver.com', '낯선 닭', 'MALE', 'USER', 40);

/*
 1. kdo0422@nate.com => work, course, relationship
 2. dog@naver.com => break_love, love
 3. cat@naver.com => relationship, break_love, etc
 4. pig@naver.com => work
 5. cow@naver.com => study, family
 6. chicken@naver.com => work, love
 */
INSERT INTO MEMBER_WORRY(member_id, worry_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 3),
       (3, 4),
       (3, 8),
       (4, 1),
       (5, 6),
       (5, 7),
       (6, 1),
       (6, 5);

INSERT INTO LETTER(uuid, sender_id, receiver_id, content, create_date, worry_type,
                   has_replied, is_delete_by_sender, is_stored, reply_content)
VALUES ('1', 2, 1, '안녕', now(), 'WORK', true, false, true, '안녕하세요!'),
       ('1', 3, 1, '안녕', now(), 'LOVE', true, false, true, '안녕하세요!'),
       ('1', 5, 1, '안녕', now(), 'BREAK_LOVE', false, false, false, ''),
       ('2', 1, 2, '안녕', now(), 'LOVE', false, false, false, ''),
       ('2', 1, 3, '안녕', now(), 'WORK', false, false, false, ''),
       ('2', 1, 4, '안녕', now(), 'FAMILY', false, false, false, ''),
       ('2', 1, 5, '안녕', now(), 'ETC', false, false, false, '');


