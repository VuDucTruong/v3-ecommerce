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
GROUP BY product_id order by product_id;


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


select * from categories;

select * from product_items ;


select * from product_items_used;

select * from accounts;
select *
from product_favorites;