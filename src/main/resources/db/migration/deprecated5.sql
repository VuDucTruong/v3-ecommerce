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
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (1, '2025-04-17 14:03:37.543768', NULL, '2026-04-17', 'phong@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'ADMIN', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (2, '2025-04-17 14:11:35.385778', NULL, '2026-04-17', 'customer@gmail.com', '2025-04-17', false, NULL, NULL, 'string', 'CUSTOMER', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (6, '2025-05-08 10:59:23.342558', NULL, NULL, 'cus1@gmail.com', NULL, false, NULL, NULL, 'string', 'CUSTOMER', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (8, '2025-05-08 11:08:58.873873', NULL, NULL, 'cus2@gmail.com', NULL, false, NULL, NULL, '123456', 'CUSTOMER', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (9, '2025-05-08 11:16:25.04038', NULL, NULL, '21522458@gm.uit.edu.vn', NULL, false, NULL, NULL, '123456789', 'CUSTOMER', NULL);


--
-- Data for Name: genre1; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (1, '2025-04-25 15:19:19.259669', NULL, 'social');


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (1, '2025-04-17 14:03:37.543768', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (2, '2025-04-17 14:06:24.94946', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (4, '2025-04-17 14:09:04.700595', NULL, 1, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (5, '2025-04-17 14:11:35.385778', NULL, 2, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (3, '2025-04-17 14:07:38.571169', NULL, 6, 'abc', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (6, '2025-05-08 11:08:58.873873', NULL, 8, 'p', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (7, '2025-05-08 11:16:25.04038', NULL, 9, 'thanh phong', NULL);


--
-- Data for Name: blogs; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (1, '2025-04-17 14:54:55.201424', NULL, 'math', NULL, 'edu');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (2, '2025-04-17 14:57:37.941007', '2025-04-17 14:57:43.135256', '', NULL, 'deleted ones');


--
-- Data for Name: product_description; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (1, '2025-04-17 16:13:40.831946', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (2, '2025-04-17 16:44:12.911651', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (3, '2025-04-17 16:49:31.057025', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (13, '2025-05-11 17:06:00.69428', NULL, 'abc', 'string', 'platform', 'string', 'tutor');


--
-- Data for Name: product_groups; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (1, '2025-04-23 21:05:23.825715', NULL, 'youtube-spotify');


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (10, '2025-04-24 11:48:34.205537', NULL, 1, NULL, 'phong youtube', 100.00, 50.00, 1, 2, false, 'phong-youtube', '["str"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (9, '2025-04-23 21:09:11.903325', NULL, 1, NULL, 'thanh phong', 1991.00, 99.00, 2, 4, false, '', '[]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (8, '2025-04-17 16:49:31.057025', NULL, NULL, NULL, 'the jakarta product', 1000.00, 50.00, 3, 2, false, 'the-jakarta-product', '[]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (14, '2025-05-11 17:06:00.69428', NULL, 1, NULL, 'Youtube', 12.99, 9.99, 13, 0, true, 'youtube', '["digital", "music", "streaming"]');


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (2, '2025-04-25 16:21:07.625201', NULL, 1, 'string', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (3, '2025-04-25 16:23:52.76518', NULL, 4, '123', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (4, '2025-04-25 16:40:36.213105', NULL, 4, '123', 2, 9);


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
-- Data for Name: notification_prod_keys; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.orders (id, created_at, deleted_at, amount, coupon_id, original_amount, profile_id, request_info, status) VALUES (1, '2025-05-07 20:08:05.216177', NULL, 100.00, NULL, 100.00, NULL, '{"email": "string", "additionalProp2": "string", "additionalProp3": "string"}', 'PROCESSING');


--
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.order_details (id, order_id, original_price, price, product_id, quantity) VALUES (1, 1, 1000.00, 50.00, 8, 1);


--
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.payments (id, created_at, deleted_at, bank_code, card_type, detail_code, detail_message, note, order_id, payment_method, payment_url, profile_id, secure_hash, status, trans_ref) VALUES (2, '2025-05-09 09:13:38.952906', NULL, NULL, NULL, NULL, NULL, 'hello', 1, 'VNPAY', 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=10000&vnp_Command=pay&vnp_CreateDate=20250509091348&vnp_CurrCode=VND&vnp_ExpireDate=20250509092848&vnp_IpAddr=0%3A0%3A0%3A0%3A0%3A0%3A0%3A1&vnp_Locale=en&vnp_OrderInfo=hello&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A3007%2Fcart%2Fpayment&vnp_TmnCode=ETGKRGNL&vnp_TxnRef=09c4a7a6-e3b1-465b-a057-f8d0c3be18da&vnp_Version=2.1.0&vnp_SecureHash=b1e3b4ac5db577bda68e5ed3fdede448a542f4b2fd0a275c0676a7390cb795bd8a798bafc607606ba0a76c0d370ac203aef38399863af20c0cd99c07d24b7e80', 3, NULL, NULL, '09c4a7a6-e3b1-465b-a057-f8d0c3be18da');


--
-- Data for Name: product_favorites; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: product_items; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (1, '2025-04-24 10:31:49.861036', 9, 'key1', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (2, '2025-04-24 10:31:49.861036', 9, 'key2', 'EU');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (27, '2025-04-24 18:49:27.142761', 10, 'string2', 'string2');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (37, '2025-04-25 08:48:52.727791', 9, '1', '1');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (38, '2025-04-25 08:48:52.727791', 9, '2', '2');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (39, '2025-04-25 08:48:52.727791', 10, '3', '3');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (58, '2025-05-02 00:22:59.395296', 8, 'SPOTIFYKEY123', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (60, '2025-05-02 00:44:07.859538', 8, 'SPOTIFY_50', 'US');


--
-- Data for Name: product_items_used; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (1, '2025-05-11 22:09:29.323963', NULL, 14, 'Y1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (2, '2025-05-11 22:09:29.323963', NULL, 14, 'Y2', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (3, '2025-05-11 22:47:56.497701', NULL, 14, 'Y4', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (4, '2025-05-11 22:50:24.539057', NULL, 14, 'Y5', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (5, '2025-05-11 22:51:46.131726', NULL, 14, 'Y6', 'US');


--
-- Data for Name: products_categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 10);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 10);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 9);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 14);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 14);


--
-- Name: accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.accounts_id_seq', 9, true);


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
-- Name: notification_prod_keys_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.notification_prod_keys_id_seq', 1, false);


--
-- Name: order_details_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.order_details_id_seq', 1, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.orders_id_seq', 1, true);


--
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.payments_id_seq', 2, true);


--
-- Name: product_description_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_description_id_seq', 13, true);


--
-- Name: product_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_groups_id_seq', 1, true);


--
-- Name: product_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_items_id_seq', 63, true);


--
-- Name: product_items_used_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_items_used_id_seq', 5, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.products_id_seq', 14, true);


--
-- Name: profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.profiles_id_seq', 7, true);


--
-- PostgreSQL database dump complete
--

