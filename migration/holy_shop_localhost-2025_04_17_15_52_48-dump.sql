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

INSERT INTO public.accounts (disable_date, enable_date, is_verified, otp, created_at, deleted_at, id, otp_expiry, password, role, email) VALUES ('2026-04-17', '2025-04-17', false, NULL, '2025-04-17 14:03:37.543768', NULL, 1, NULL, 'string', 'ROLE_CUSTOMER', 'customer1@gmail.com');
INSERT INTO public.accounts (disable_date, enable_date, is_verified, otp, created_at, deleted_at, id, otp_expiry, password, role, email) VALUES ('2026-04-17', '2025-04-17', false, NULL, '2025-04-17 14:06:24.94946', NULL, 2, NULL, 'string', 'ROLE_CUSTOMER', 'customer1@gmail.com');
INSERT INTO public.accounts (disable_date, enable_date, is_verified, otp, created_at, deleted_at, id, otp_expiry, password, role, email) VALUES ('2026-04-17', '2025-04-17', false, NULL, '2025-04-17 14:07:38.571169', NULL, 3, NULL, 'string', 'ROLE_CUSTOMER', 'customer1@gmail.com');
INSERT INTO public.accounts (disable_date, enable_date, is_verified, otp, created_at, deleted_at, id, otp_expiry, password, role, email) VALUES ('2026-04-17', '2025-04-17', false, NULL, '2025-04-17 14:09:04.700595', NULL, 4, NULL, 'string', 'ROLE_CUSTOMER', 'customer1@gmail.com');
INSERT INTO public.accounts (disable_date, enable_date, is_verified, otp, created_at, deleted_at, id, otp_expiry, password, role, email) VALUES ('2026-04-17', '2025-04-17', false, NULL, '2025-04-17 14:11:35.385778', NULL, 5, NULL, 'string', 'ROLE_CUSTOMER', 'customer1@gmail.com');


--
-- Data for Name: blogs; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: product_description; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.categories (created_at, deleted_at, id, product_id, description, image_url_id, name) VALUES ('2025-04-17 14:54:55.201424', NULL, 1, NULL, 'math', NULL, 'edu');
INSERT INTO public.categories (created_at, deleted_at, id, product_id, description, image_url_id, name) VALUES ('2025-04-17 14:57:37.941007', '2025-04-17 14:57:43.135256', 2, NULL, '', NULL, 'deleted ones');


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.profiles (account_id, created_at, deleted_at, id, full_name, image_url_id, phone) VALUES (NULL, '2025-04-17 14:03:37.543768', NULL, 1, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (account_id, created_at, deleted_at, id, full_name, image_url_id, phone) VALUES (NULL, '2025-04-17 14:06:24.94946', NULL, 2, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (account_id, created_at, deleted_at, id, full_name, image_url_id, phone) VALUES (NULL, '2025-04-17 14:07:38.571169', NULL, 3, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (account_id, created_at, deleted_at, id, full_name, image_url_id, phone) VALUES (NULL, '2025-04-17 14:09:04.700595', NULL, 4, 'phong', NULL, '01235567891');
INSERT INTO public.profiles (account_id, created_at, deleted_at, id, full_name, image_url_id, phone) VALUES (NULL, '2025-04-17 14:11:35.385778', NULL, 5, 'phong', NULL, '01235567891');


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: phong
--



--
-- Data for Name: coupons; Type: TABLE DATA; Schema: public; Owner: phong
--




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

SELECT pg_catalog.setval('public.coupons_id_seq', 1, false);


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

SELECT pg_catalog.setval('public.product_description_id_seq', 1, false);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.products_id_seq', 1, false);


--
-- Name: profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.profiles_id_seq', 5, true);


--
-- PostgreSQL database dump complete
--

