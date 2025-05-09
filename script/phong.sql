
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

select *
from products p
         join product_description pd
              on p.prod_desc_id = pd.id
DROP TABLE IF EXISTS products CASCADE;



select product_id, count(1) as cnt
from product_items
where product_id IN (9)
GROUP BY product_id

-- First, declare a variable to hold the result
DO
$$
    DECLARE
        rejected_keys TEXT[];
    BEGIN
        -- Call the procedure
        CALL insert_product_items_with_conflict_detection(
                ARRAY [9,9]::BIGINT[], -- product_ids
                ARRAY ['key1', 'key2']::TEXT[], -- product_keys
                ARRAY ['US', 'EU']::TEXT[], -- regions
                rejected_keys -- OUT parameter to receive the results
             );

        -- Display the result
        RAISE NOTICE 'Rejected keys: %', rejected_keys;
    END
$$;

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


select p1_0.id,
       c1_0.product_id,
       c1_1.id,
       c1_1.created_at,
       c1_1.deleted_at,
       c1_1.description,
       c1_1.image_url_id,
       c1_1.name,
       p1_0.created_at,
       p1_0.deleted_at,
       g1_0.id,
       g1_0.created_at,
       g1_0.deleted_at,
       g1_0.name,
       p1_0.group_id,
       p1_0.image_url_id,
       p1_0.name,
       p1_0.original_price,
       p1_0.price,
       pd1_0.id,
       pd1_0.created_at,
       pd1_0.deleted_at,
       pd1_0.description,
       pd1_0.info,
       pd1_0.platform,
       pd1_0.policy,
       pd1_0.product_id,
       pd1_0.tutorial,
       p1_0.quantity,
       p1_0.represent,
       p1_0.slug,
       p1_0.tags
from public.products p1_0
         left join public.products_categories c1_0 on p1_0.id = c1_0.product_id
         left join public.categories c1_1 on c1_1.id = c1_0.category_id
         left join public.product_groups g1_0 on g1_0.id = p1_0.group_id
         left join public.products v1_0 on g1_0.id = v1_0.group_id
         left join public.product_description pd1_0 on p1_0.id = pd1_0.product_id
where p1_0.id=8
  and p1_0.deleted_at is null;



select *
from accounts a
join profiles p
on a.id = p.account_id
where email='phong@gmail.com';

select * from profiles;

select *
from payments;

delete
from payments
where true;

select *
from orders;