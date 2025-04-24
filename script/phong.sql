

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

select * from
flyway_schema_history;

delete from flyway_schema_history;

update products
SET prod_desc_id = 6
where id = 8;

select * from categories;

select * from accounts;
select * from coupons;

select * from product_description pd;

select * from products p join product_description pd
    on p.prod_desc_id = pd.id
DROP TABLE IF EXISTS products CASCADE;



select product_id, count(1) as cnt
from product_items where product_id IN (9)
GROUP BY product_id

-- First, declare a variable to hold the result
DO $$
    DECLARE
        rejected_keys TEXT[];
    BEGIN
        -- Call the procedure
        CALL insert_product_items_with_conflict_detection(
                ARRAY[9,9]::BIGINT[], -- product_ids
                ARRAY['key1', 'key2']::TEXT[], -- product_keys
                ARRAY['US', 'EU']::TEXT[], -- regions
                rejected_keys -- OUT parameter to receive the results
             );

        -- Display the result
        RAISE NOTICE 'Rejected keys: %', rejected_keys;
    END $$;

select * from product_items


SELECT * FROM insert_product_items_with_conflict_detection(
        ARRAY(1, 2, 3)::BIGINT[],
        ARRAY['key1', 'key2', 'key3']::TEXT[],
        ARRAY['US', 'EU', 'ASIA']::TEXT[]
              );

select * from product_items;