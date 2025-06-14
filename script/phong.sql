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
from profiles;

select *
from payments;

delete
from payments
where true;

select *
from products p
         join product_description pd on p.prod_desc_id = pd.id;


select *
from product_items;


select *
from product_items_used;
select *
from payments
where trans_ref = '09c4a7a6-e3b1-465b-a057-f8d0c3be18da'
select *
from accounts;
select *
from product_favorites;

select *
from product_favorites
where product_id = 8;


select *
from products;
select *
from comments;
select *
from public.blogs b1_0
         join public.blogs_genres g1_0 on b1_0.id = g1_0.blog_id
         join public.genre2 g1_1 on g1_1.id = g1_0.genre2_id
         join public.genre1 g2_0 on g2_0.id = g1_1.genre1_id
         join public.profiles p1_0 on p1_0.id = b1_0.profile_id
where 1 = 1
  and b1_0.deleted_at is null
order by b1_0.id;

select *
from genre2;
SELECT *
FROM blogs_genres
where blog_id = 6;
select *
from blogs;


select *;
select *
from product_items_used
where product_id = 8;


select *
from products
order by id;


select *
from product_items
order by id desc;

select *
from categories;
select *
from products_categories
where product_id = 20;

select *
from coupons;
select *
from orders;
select *
from blogs;

SELECT * FROM products p
                  LEFT JOIN product_tags pt on pt.product_id = p.id
WHERE p.id IN (select od.product_id from order_details od
               group by od.product_id
               ORDER BY SUM(od.quantity) desc)

SELECT * FROM (
                  SELECT od.product_id, SUM(od.quantity) AS totalSold
                  FROM order_details od
                  GROUP BY od.product_id
                  ORDER BY totalSold DESC
                  LIMIT 10
              ) t
                  RIGHT JOIN products p ON t.product_id = p.id
                  LEFT JOIN product_tags pt ON pt.product_id = p.id
ORDER BY t.totalSold IS NULL, t.totalSold DESC
LIMIT 10;

SELECT * from orders o
join order_details od
on o.id = od.order_id
where order_id = 9;

select * from order_details;
SELECT * from orders;

SELECT * FROM products p
where p.slug in ('a', 'a-very-long-product');

SELECT * from payments;