

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


select * from accounts;
select * from coupons;