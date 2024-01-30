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
VALUES (now(), 0, 0, 'dog@naver.com', '낯선 강아지', 'MALE', 'USER', 20),
       (now(), 0, 0, 'cat@naver.com', '낯선 고양이', 'MALE', 'USER', 20),
       (now(), 0, 0, 'pig@naver.com', '낯선 돼지', 'MALE', 'USER', 100),
       (now(), 0, 0, 'cow@naver.com', '낯선 소', 'MALE', 'USER', 30),
       (now(), 0, 0, 'chicken@naver.com', '낯선 닭', 'MALE', 'USER', 40);

/*
 dog@naver.com => work, course, relationship
 cat@naver.com => break_love, love
 pig@naver.com => relationship, break_love, etc
 cow@naver.com => work
 chicken@naver.com => study, family
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
       (5, 7);
