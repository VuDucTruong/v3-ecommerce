-- PostgreSQL database dump
--

-- Dumped from database version 16.8 (Ubuntu 16.8-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.8 (Ubuntu 16.8-0ubuntu0.24.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-----Procedures

-- Drop the function if it exists
DROP FUNCTION IF EXISTS public.insert_product_items_with_conflict_detection(BIGINT[], TEXT[], TEXT[]);
CREATE OR REPLACE FUNCTION public.insert_product_items_with_conflict_detection(
    product_ids BIGINT[],
    product_keys TEXT[],
    regions TEXT[]
)
    RETURNS TABLE (rejected_product_key TEXT)
    LANGUAGE plpgsql
AS $$
DECLARE
    temp_row RECORD;
    inserted_keys TEXT[] := ARRAY[]::TEXT[];
BEGIN
    -- Create temporary table to hold input data
    CREATE TEMP TABLE temp_product_items
    (
        product_id  BIGINT,
        product_key TEXT,
        region      TEXT
    ) ON COMMIT DROP;

    -- Insert data from arrays into temporary table
    INSERT INTO temp_product_items (product_id, product_key, region)
    SELECT product_ids[i], product_keys[i], regions[i]
    FROM generate_subscripts(product_ids, 1) as i
    WHERE i <= least(
            coalesce(array_length(product_ids, 1), 0),
            coalesce(array_length(product_keys, 1), 0),
            coalesce(array_length(regions, 1), 0)
               );

    -- Insert each row individually and track successful inserts
    FOR temp_row IN SELECT * FROM temp_product_items LOOP
            BEGIN
                INSERT INTO public.product_items (product_id, product_key, region)
                VALUES (temp_row.product_id, temp_row.product_key, temp_row.region)
                ON CONFLICT (product_key) DO NOTHING
                RETURNING product_key INTO temp_row.product_key;

                IF temp_row.product_key IS NOT NULL THEN
                    inserted_keys := array_append(inserted_keys, temp_row.product_key);
                END IF;
            EXCEPTION
                WHEN foreign_key_violation THEN
                -- Do nothing, this key will be returned as rejected
            END;
        END LOOP;

    -- Return all keys that weren't successfully inserted
    RETURN QUERY
        SELECT t.product_key
        FROM temp_product_items t
        WHERE t.product_key <> ALL(COALESCE(inserted_keys, ARRAY[]::TEXT[]));
END;
$$;

-----------------------------------End Procedures-----------------------------------

--
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry,
                             password, role)
VALUES (1, '2025-04-17 14:03:37.543768', NULL, '2026-04-17',
        'phong@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'ROLE_ADMIN');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry,
                             password, role)
VALUES (2, '2025-04-17 14:11:35.385778', NULL, '2026-04-17',
        'customer@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'ROLE_CUSTOMER');


--
-- Data for Name: blogs; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name)
VALUES (1, '2025-04-17 14:54:55.201424', NULL, 'math', NULL, 'edu');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name)
VALUES (2, '2025-04-17 14:57:37.941007', '2025-04-17 14:57:43.135256', '', NULL, 'deleted ones');



--
-- Data for Name: product_groups; Type: TABLE DATA; Schema: public; Owner: phong
--
INSERT INTO public.product_groups (id, created_at, deleted_at, name)
VALUES (1, '2025-04-23 21:05:23.825715', NULL, 'youtube-spotify');


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name,
                             original_price, price, slug, tags, is_represent)
VALUES (8, '2025-04-17 16:49:31.057025', NULL, NULL, NULL,
        'the jakarta product', 1000.00, 50.00,
        'the-jakarta-product', '[]', false);
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name,
                             original_price, price, slug, tags, is_represent)
VALUES (10, '2025-04-24 11:48:34.205537', NULL, 1, NULL,
        'phong youtube', 100.00, 50.00,
        'phong-youtube', '[
    "str"
  ]', false);
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name,
                             original_price, price, slug, tags, is_represent)
VALUES (9, '2025-04-23 21:09:11.903325', NULL, 1, NULL,
        'thanh phong', 1991.00, 99.00,
        '', '[]', true);

---

--
-- Data for Name: product_description; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial)
VALUES (1, '2025-04-17 16:13:40.831946', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial)
VALUES (2, '2025-04-17 16:44:12.911651', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial)
VALUES (6, '2025-04-17 16:49:31.057025', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial)
VALUES (7, '2025-04-23 21:09:11.903325', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial)
VALUES (8, '2025-04-24 11:48:34.205537', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial,
                                        product_id)
VALUES (9, '2025-04-24 11:51:10.273004', NULL,
        'string', 'string', 'string', 'string', 'string', 9);



--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id, phone)
VALUES (1, '2025-04-17 14:03:37.543768', NULL,
        NULL, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id, phone)
VALUES (2, '2025-04-17 14:06:24.94946', NULL,
        NULL, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id, phone)
VALUES (3, '2025-04-17 14:07:38.571169', NULL,
        NULL, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id, phone)
VALUES ( 4, '2025-04-17 14:09:04.700595', NULL
       , 1, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id, phone)
VALUES (5, '2025-04-17 14:11:35.385778', NULL,
        2, 'phong', NULL, '01235567891');


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: coupons; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.coupons (id, created_at, deleted_at, available_from, available_to, code, current_usage, description,
                            max_applied_amount, min_amount, type, usage_limit, value)
VALUES (1, '2025-04-17 22:25:17.232006', NULL, '2025-04-17', '2025-04-17', 'PHONG', 0,
        'string', 20000.00, 156000.00,
        'PERCENTAGE', 1, 12.00);


--
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: keywords; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: product_favorites; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: product_items; Type: TABLE DATA; Schema: public; Owner: phong
--


--
-- Data for Name: products_categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products_categories (category_id, product_id)
VALUES (1, 8);
INSERT INTO public.products_categories (category_id, product_id)
VALUES (2, 8);
INSERT INTO public.products_categories (category_id, product_id)
VALUES (1, 10);
INSERT INTO public.products_categories (category_id, product_id)
VALUES (2, 10);
INSERT INTO public.products_categories (category_id, product_id)
VALUES (2, 9);


--
-- Name: accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.accounts_id_seq', 5, true);


--
-- Name: blogs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.blogs_id_seq', 1, false);


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.categories_id_seq', 2, true);


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.comments_id_seq', 1, false);


--
-- Name: coupons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.coupons_id_seq', 1, true);


--
-- Name: genre_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.genre_id_seq', 1, false);


--
-- Name: keywords_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.keywords_id_seq', 1, false);


--
-- Name: order_details_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.order_details_id_seq', 1, false);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.orders_id_seq', 1, false);


--
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.payments_id_seq', 1, false);


--
-- Name: product_description_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_description_id_seq', 9, true);


--
-- Name: product_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_groups_id_seq', 1, true);


--
-- Name: product_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_items_id_seq', 1, false);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.products_id_seq', 10, true);


--
-- Name: profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.profiles_id_seq', 5, true);


--
-- PostgreSQL database dump complete
--