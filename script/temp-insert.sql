-- Order 1: Single product with discount
BEGIN;
INSERT INTO orders (amount, original_amount, status)
VALUES (9.99, 12.99, 'COMPLETED') RETURNING id;

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 12.99, 9.99, 19, 1);
COMMIT;

-- Order 2: Multiple products with significant discount
BEGIN;
INSERT INTO orders (amount, original_amount, status)
VALUES (109.98, 2013.98, 'COMPLETED') RETURNING id;

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 12.99, 9.99, 19, 2);

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 1991.00, 99.00, 9, 1);
COMMIT;

-- Order 3: High-value single product with small discount (price > original price)
BEGIN;
INSERT INTO orders (amount, original_amount, status)
VALUES (100000.00, 99800.00, 'COMPLETED') RETURNING id;

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 99800.00, 100000.00, 20, 1);
COMMIT;

-- Order 4: Mixed order with both discounted and premium products
BEGIN;
INSERT INTO orders (amount, original_amount, status)
VALUES (123465.99, 200141.99, 'COMPLETED') RETURNING id;

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 12.99, 9.99, 19, 1);

INSERT INTO order_details (order_id, original_price, price, product_id, quantity)
VALUES (currval('orders_id_seq'), 200123.00, 123456.00, 29, 1);
COMMIT;



select id, price, original_price
from products where deleted_at is null;


