--
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

--
    --- triggers:
    ---
-- On insert



--
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES (1, '2025-04-17 14:03:37.543768', NULL, '2026-04-17', 'phong@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'ADMIN');
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role) VALUES (2, '2025-04-17 14:11:35.385778', NULL, '2026-04-17', 'customer@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'CUSTOMER');


--
-- Data for Name: genre1; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (1, '2025-04-25 15:19:19.259669', NULL, 'social');


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (1, '2025-04-17 14:03:37.543768', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (2, '2025-04-17 14:06:24.94946', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (3, '2025-04-17 14:07:38.571169', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (4, '2025-04-17 14:09:04.700595', NULL, 1, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (5, '2025-04-17 14:11:35.385778', NULL, 2, 'phong', NULL);


--
-- Data for Name: blogs; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (1, '2025-04-17 14:54:55.201424', NULL, 'math', NULL, 'edu');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (2, '2025-04-17 14:57:37.941007', '2025-04-17 14:57:43.135256', '', NULL, 'deleted ones');


--
-- Data for Name: product_groups; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (1, '2025-04-23 21:05:23.825715', NULL, 'youtube-spotify');


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, is_represent, name, original_price, price, slug, tags) VALUES (8, '2025-04-17 16:49:31.057025', NULL, NULL, NULL, false, 'the jakarta product', 1000.00, 50.00, 'the-jakarta-product', '[]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, is_represent, name, original_price, price, slug, tags) VALUES (10, '2025-04-24 11:48:34.205537', NULL, 1, NULL, false, 'phong youtube', 100.00, 50.00, 'phong-youtube', '["str"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, is_represent, name, original_price, price, slug, tags) VALUES (9, '2025-04-23 21:09:11.903325', NULL, 1, NULL, true, 'thanh phong', 1991.00, 99.00, '', '[]');


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.comments (id, created_at, deleted_at, author_id, parent_comment_id, product_id, content) VALUES (2, '2025-04-25 16:21:07.625201', NULL, 1, NULL, 9, 'string');
INSERT INTO public.comments (id, created_at, deleted_at, author_id, parent_comment_id, product_id, content) VALUES (3, '2025-04-25 16:23:52.76518', NULL, 4, NULL, 9, '123');
INSERT INTO public.comments (id, created_at, deleted_at, author_id, parent_comment_id, product_id, content) VALUES (4, '2025-04-25 16:40:36.213105', NULL, 4, 2, 9, '123');


--
-- Data for Name: coupons; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.coupons (id, created_at, deleted_at, available_from, available_to, code, current_usage, description, max_applied_amount, min_amount, type, usage_limit, value) VALUES (1, '2025-04-17 22:25:17.232006', NULL, '2025-04-17', '2025-04-17', 'PHONG', 0, 'string', 20000.00, 156000.00, 'PERCENTAGE', 1, 12.00);


--
-- Data for Name: genre2; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (3, '2025-04-25 15:23:37.771039', NULL, 1, 'life');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (4, '2025-04-25 15:23:37.771039', NULL, 1, 'news');


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
-- Data for Name: product_description; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (1, '2025-04-17 16:13:40.831946', NULL, 'string', 'string', 'string', 'string', NULL, 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (2, '2025-04-17 16:44:12.911651', NULL, 'string', 'string', 'string', 'string', NULL, 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (6, '2025-04-17 16:49:31.057025', NULL, 'string', 'string', 'string', 'string', NULL, 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (7, '2025-04-23 21:09:11.903325', NULL, 'string', 'string', 'string', 'string', NULL, 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (8, '2025-04-24 11:48:34.205537', NULL, 'string', 'string', 'string', 'string', NULL, 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, product_id, tutorial) VALUES (9, '2025-04-24 11:51:10.273004', NULL, 'string', 'string', 'string', 'string', 9, 'string');


--
-- Data for Name: product_favorites; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: product_items; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (1, '2025-04-24 10:31:49.861036', NULL, NULL, 9, 'key1', 'US');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (2, '2025-04-24 10:31:49.861036', NULL, NULL, 9, 'key2', 'EU');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (23, '2025-04-24 18:05:13.712308', NULL, NULL, NULL, 'string', 'string');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (24, '2025-04-24 18:07:28.380482', NULL, NULL, NULL, '["string"]', '["string"]');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (25, '2025-04-24 18:17:19.965505', NULL, NULL, NULL, '(string,string2)', '(string,string2)');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (27, '2025-04-24 18:49:27.142761', NULL, NULL, 10, 'string2', 'string2');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (37, '2025-04-25 08:48:52.727791', NULL, NULL, 9, '1', '1');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (38, '2025-04-25 08:48:52.727791', NULL, NULL, 9, '2', '2');
INSERT INTO public.product_items (id, created_at, deleted_at, date_used, product_id, product_key, region) VALUES (39, '2025-04-25 08:48:52.727791', NULL, NULL, 10, '3', '3');


--
-- Data for Name: products_categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 10);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 10);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 9);


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

SELECT pg_catalog.setval('public.comments_id_seq', 4, true);


--
-- Name: coupons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.coupons_id_seq', 1, true);


--
-- Name: genre1_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.genre1_id_seq', 1, true);


--
-- Name: genre2_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.genre2_id_seq', 4, true);


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

SELECT pg_catalog.setval('public.product_items_id_seq', 39, true);


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

