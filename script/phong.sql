SELECT *
FROM flyway_schema_history;
select *
from accounts;

select *
from profiles;
select *
from products p
         join product_description pd on p.prod_desc_id = p.id;

select *
from product_description;

select *
from flyway_schema_history;

delete
from flyway_schema_history;

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


SELECT *
FROM insert_product_items_with_conflict_detection(
        ARRAY(1, 2, 3)::BIGINT[],
        ARRAY ['key1', 'key2', 'key3']::TEXT[],
        ARRAY ['US', 'EU', 'ASIA']::TEXT[]
     );

select *
from product_items;

select *
from coupons;

select *
from genre;
select *
from genre2


select c1_0.id,
       c1_0.author_id,
       c1_0.content,
       c1_0.created_at,
       c1_0.deleted_at,
       c1_0.parent_comment_id,
       c1_0.product_id,
       p3_0.id,
       p3_0.account_id,
       p3_0.created_at,
       p3_0.deleted_at,
       p3_0.full_name,
       p3_0.image_url_id,
       p3_0.phone,
       r1_0.parent_comment_id,
       r1_0.id,
       r1_0.author_id,
       r1_0.content,
       r1_0.created_at,
       r1_0.deleted_at,
       r1_0.product_id
from public.comments c1_0
         left join public.profiles p3_0 on p3_0.id = c1_0.author_id
         left join public.comments r1_0 on c1_0.id = r1_0.parent_comment_id
where c1_0.id=3
