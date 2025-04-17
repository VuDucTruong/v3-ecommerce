insert into accounts (created_at, deleted_at, disable_date, email, enable_date, is_verified, password, role,
                             otp, otp_expiry)
values ('2025-04-11 09:17:42.408720', null, null, 'phong@gm.com', null, true, '1234', 'ROLE_ADMIN', null, null),
       ('2025-04-11 09:17:42.448173', null, null, 'abc@gmail.com', null, true, '1234', 'ROLE_USER', null, null),
       ('2025-04-11 09:24:21.328508', null, null, 'string', null, true, 'string', 'ROLE_ADMIN', null, null);

select * from accounts;

select * from blogs;

INSERT INTO blogs
(created_at, deleted_at, title, content, image_url_id, published_at)
VALUES ('2025-04-11 09:17:42.408720', null, 'title1',
        'content1', 1, '2025-04-11 09:17:42.408720'),
       ('2025-04-11 09:17:42.448173', null, 'title2',
        'content2', 2, '2025-04-11 09:17:42.448173'),
       ('2025-04-11 09:24:21.328508', null, 'title3',
        'content3', 3, '2025-04-11 09:24:21.328508');

INSERT INTO categories
(description, image_url_id, name)
VALUES ('description1', 1, 'category1'),
       ('description2', 2, 'category2'),
       ('description3', 3, 'category3');


drop table flyway_schema_history;

delete from products;


SELECT * FROM flyway_schema_history;
select * from accounts;

select * from profiles;
select * from products;