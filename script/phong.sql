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
FROM products p
         LEFT JOIN product_tags pt on pt.product_id = p.id
WHERE p.id IN (select od.product_id
               from order_details od
               group by od.product_id
               ORDER BY SUM(od.quantity) desc)

SELECT *
FROM (SELECT od.product_id, SUM(od.quantity) AS totalSold
      FROM order_details od
      GROUP BY od.product_id
      ORDER BY totalSold DESC
      LIMIT 10) t
         RIGHT JOIN products p ON t.product_id = p.id
         LEFT JOIN product_tags pt ON pt.product_id = p.id
ORDER BY t.totalSold IS NULL, t.totalSold DESC
LIMIT 10;

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

select *
from public.orders o1_0
         left join public.payments p1_0 on o1_0.id = p1_0.order_id
where o1_0.created_at<'2025-05-22 00:00:00.0'
  and o1_0.created_at>'2025-06-21 23:59:59.999';

select * FROM orders;

SELECT * FROM blogs;