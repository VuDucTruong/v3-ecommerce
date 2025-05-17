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


select b1_0.id,
       b1_0.content,
       b1_0.created_at,
       b1_0.deleted_at,
       g1_0.id,
       g1_0.created_at,
       g1_0.deleted_at,
       g2_0.genre1_id,
       g2_0.id,
       g2_0.created_at,
       g2_0.deleted_at,
       g2_0.name,
       g1_0.name,
       b1_0.genre_id,
       b1_0.image_url_id,
       p1_0.id,
       p1_0.account_id,
       p1_0.created_at,
       p1_0.deleted_at,
       p1_0.full_name,
       p1_0.image_url_id,
       b1_0.profile_id,
       b1_0.published_at,
       b1_0.subtitle,
       b1_0.title
from public.blogs b1_0
         join public.genre1 g1_0 on g1_0.id = b1_0.genre_id
         left join public.genre2 g2_0 on g1_0.id = g2_0.genre1_id
         join public.profiles p1_0 on p1_0.id = b1_0.profile_id
where 1 = 1
  and b1_0.title like ? escape
      '' and g1_0.name like ? escape '' and b1_0.published_at>=? and b1_0.published_at<=? and b1_0.deleted_at is null
order by b1_0.id
