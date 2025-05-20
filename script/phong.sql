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
from public.products p1_0
         left join public.products_categories c1_0 on p1_0.id = c1_0.product_id
         left join public.categories c1_1 on c1_1.id = c1_0.category_id
         left join public.product_description pd1_0 on pd1_0.id = p1_0.prod_desc_id
where p1_0.represent=true
  and p1_0.id in (1,17)
  and p1_0.deleted_at is null
order by p1_0.id