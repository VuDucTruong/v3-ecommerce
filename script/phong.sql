update products
SET prod_desc_id = 6
where id = 8;

select *
from categories;

select *
from accounts;
select *
from coupons;

select *
from product_description pd;


select product_id, count(1) as cnt
from product_items
where product_id IN (9)
GROUP BY product_id
order by product_id;


select *
from accounts a
         join profiles p
              on a.id = p.account_id;

select *
from product_items;

select *
from coupons;

select *
from genre1;
select *
from genre2;

select *
from products p
         left join product_items pi
                   ON p.id = pi.product_id;

UPDATE products p
SET quantity =
            (select count(1) from product_items pi where pi.product_id = p.id)
where true;


delete
from product_items
where product_id = 8;

select *
from accounts a
         join profiles p
              on a.id = p.account_id
where email = 'phong@gmail.com';


select *
from product_items_used;
select *
from payments
where trans_ref = '09c4a7a6-e3b1-465b-a057-f8d0c3be18da';
select *
from accounts;
select *
from product_favorites;


select *
from products;
select *
from comments;



SELECT *
from orders o
         join order_details od
              on o.id = od.order_id
where order_id = 9;

select *
from order_details;
SELECT *
from orders;

SELECT *
FROM products p
where p.slug in ('a', 'a-very-long-product');

-- DELETE FROM or

select *
from orders;
SELECT *
from payments;
select *
from notification_prod_keys;
select *
from notification_prod_keys_fail;
select *
from notification_prod_keys_success;
select *
from profiles;

select *
from order_details od
where od.order_id = 9;
select *
from product_items pi
where pi.product_id = 23;

select *
from product_items_used;

select od1_0.id, od1_0.product_id, od1_0.order_id, od1_0.quantity, p1_0.name, p1_0.image_url_id
from public.order_details od1_0
         left join public.products p1_0 on od1_0.product_id = p1_0.id
where od1_0.order_id in (9);

SELECT *
FROM orders;

SELECT *
FROM notification_prod_keys_fail;
INSERT INTO notification_prod_keys_fail(id, order_id, email)
VALUES (0, 1, '21522458@gm.uit.edu.vn');

SELECT *
FROM orders
where id = 1;
SELECT *
FROM order_details
where order_id = 1;
SELECT *
FROM products
where id = 8;
SELECT *
FROM notification_prod_keys_success;

UPDATE blogs
SET deleted_at = now()
where id = 3;


SELECT * FROM accounts;

SELECT * FROM orders o
join notification_prod_keys_success npk
ON o.id = npk.order_id;

SELECT * FROM notification_prod_keys_success npks
where order_id = 6;