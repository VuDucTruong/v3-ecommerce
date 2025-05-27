--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9 (Ubuntu 16.9-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.9 (Ubuntu 16.9-0ubuntu0.24.04.1)

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
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (8, '2025-05-08 11:08:58.873873', NULL, NULL, 'cus2@gmail.com', NULL, false, NULL, NULL, '123456', 'CUSTOMER', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (9, '2025-05-08 11:16:25.04038', NULL, NULL, '21522458@gm.uit.edu.vn', NULL, false, NULL, NULL, '123456789', 'CUSTOMER', NULL);
INSERT INTO public.accounts (id, created_at, deleted_at, disable_date, email, enable_date, is_verified, otp, otp_expiry, password, role, verification_code) VALUES (6, '2025-05-08 10:59:23.342558', NULL, '2025-05-14', 'cus1@gmail.com', '2025-05-13', false, NULL, NULL, 'string', 'CUSTOMER', NULL);


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (1, '2025-04-17 14:03:37.543768', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (2, '2025-04-17 14:06:24.94946', NULL, NULL, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (5, '2025-04-17 14:11:35.385778', NULL, 2, 'phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (3, '2025-04-17 14:07:38.571169', NULL, 6, 'abc', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (6, '2025-05-08 11:08:58.873873', NULL, 8, 'p', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (7, '2025-05-08 11:16:25.04038', NULL, 9, 'thanh phong', NULL);
INSERT INTO public.profiles (id, created_at, deleted_at, account_id, full_name, image_url_id) VALUES (4, '2025-04-17 14:09:04.700595', NULL, 1, 'abc', NULL);


--
-- Data for Name: blogs; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (1, '2025-05-18 00:38:35.529595', NULL, '<p>string</p>', NULL, 4, '2025-05-18 00:30:18.654', 'string', 'string');
INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (2, '2025-05-18 00:40:01.774669', NULL, '<p>string</p>', NULL, 4, '2025-05-18 00:30:18.654', 'string', 'string');
INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (3, '2025-05-21 18:42:05.171284', NULL, '<p>alkjsdlkajskdjkajsdjfkla<p/>', NULL, 4, '2025-05-21 18:41:31.409', 'sth', 'sth');
INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (4, '2025-05-21 18:43:09.769097', NULL, '<p>alkjsdlkajskdjkajsdjfkla<p/>', NULL, 4, '2025-05-21 18:41:31.409', 'sth', 'sth');
INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (5, '2025-05-21 18:43:25.194611', NULL, '<p>alkjsdlkajskdjkajsdjfkla<p/>', NULL, 4, '2025-05-21 18:41:31.409', 'sth', 'sth');
INSERT INTO public.blogs (id, created_at, deleted_at, content, image_url_id, profile_id, published_at, subtitle, title) VALUES (6, '2025-05-21 18:45:21.956605', NULL, '<p>ok</p>', NULL, 4, '2025-05-21 18:46:53.565', 'olka', 'olka');


--
-- Data for Name: genre1; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (1, '2025-04-25 15:19:19.259669', NULL, 'Game');
INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (2, '2025-05-14 02:11:06.184225', NULL, 'Mẹo hay');
INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (3, '2025-05-14 02:11:07.893819', NULL, 'AI');
INSERT INTO public.genre1 (id, created_at, deleted_at, name) VALUES (4, '2025-05-14 02:11:09.183369', NULL, 'Movies');


--
-- Data for Name: genre2; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (1, '2025-04-25 15:23:37.771039', NULL, 1, 'Esport');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (2, '2025-04-25 15:23:37.771039', NULL, 1, 'game miễn phí');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (3, '2025-05-14 02:12:10.241431', NULL, 1, 'Mẹo chơi game');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (4, '2025-05-14 02:12:10.256542', NULL, 1, 'Tin tức game');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (5, '2025-05-14 02:12:13.593002', NULL, 2, 'Điện thoại');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (6, '2025-05-14 02:12:13.606242', NULL, 2, 'Ứng dụng');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (7, '2025-05-14 02:12:13.617468', NULL, 2, 'Máy tính');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (8, '2025-05-14 02:12:13.630135', NULL, 2, 'Phần cứng');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (9, '2025-05-14 02:12:13.641187', NULL, 2, 'Thủ thuật');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (10, '2025-05-14 02:12:16.524289', NULL, 4, 'Đánh giá');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (11, '2025-05-14 02:12:16.54024', NULL, 4, 'Giới thiệu phim');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (12, '2025-05-14 02:12:16.553861', NULL, 4, 'Netflix');
INSERT INTO public.genre2 (id, created_at, deleted_at, genre1_id, name) VALUES (13, '2025-05-14 02:12:16.567998', NULL, 4, 'Phim chiếu rạp');


--
-- Data for Name: blogs_genres; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (1, 1);
INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (2, 7);
INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (3, 3);
INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (4, 3);
INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (6, 9);
INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (6, 10);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (1, '2025-04-17 14:54:55.201424', NULL, 'math', NULL, 'edu');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (2, '2025-04-17 14:57:37.941007', '2025-04-17 14:57:43.135256', '', NULL, 'deleted ones');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (3, '2025-05-24 12:23:09.534198', NULL, 'string', NULL, 'Sport');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (4, '2025-05-24 12:23:15.88427', NULL, 'string', NULL, 'Gaming');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (5, '2025-05-24 12:23:21.197405', NULL, 'string', NULL, 'News');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (6, '2025-05-24 12:23:34.226385', NULL, 'string', NULL, 'Tradings');
INSERT INTO public.categories (id, created_at, deleted_at, description, image_url_id, name) VALUES (7, '2025-05-24 12:23:43.704757', NULL, 'string', NULL, 'Politics');


--
-- Data for Name: product_description; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (2, '2025-04-17 16:44:12.911651', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (3, '2025-04-17 16:49:31.057025', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (13, '2025-05-11 17:06:00.69428', NULL, 'abc', 'string', 'platform', 'string', 'tutor');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (15, '2025-05-12 21:32:20.404573', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (16, '2025-05-12 21:36:49.037095', NULL, '<p>This is the platform</p>', '<p>This is the tutorial</p>', '<p>This is the description</p>', '<p>This is the description</p>', '<p>This is the information</p>');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (17, '2025-05-12 21:51:07.058552', NULL, '<p>This is the platform</p>', '<p>This is the tutorial</p>', '<p>This is the description</p>', '<p>This is the description</p>', '<p>This is the information</p>');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (18, '2025-05-12 23:37:51.33737', NULL, 'string', 'string', 'string', 'string', 'string');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (19, '2025-05-13 00:13:31.037736', NULL, '<p>adsfasd</p>', '<p>asdfasd</p>', '<p>asdasd</p>', '<p>asdasd</p>', '<p>asdasd</p>');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (1, '2025-04-17 16:13:40.831946', NULL, '123123123', '123123', 'asdasd', '123123123123123130000', '123');
INSERT INTO public.product_description (id, created_at, deleted_at, description, info, platform, policy, tutorial) VALUES (21, '2025-05-24 12:23:53.166715', NULL, ' <html lang="en">  <body>     <h1>Lorem Ipsum Test Document</h1>     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>          <h2>Key Points</h2>     <ul>         <li>Lorem ipsum dolor sit amet</li>         <li>Consectetur adipiscing elit</li>         <li>Sed do eiusmod tempor incididunt</li>         <li>Ut labore et dolore magna aliqua</li>         <li>Ut enim ad minim veniam</li>     </ul>      <p>Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc.</p>      <h2>Additional Information</h2>     <ul>         <li>Proin sagittis nisl rhoncus mattis</li>         <li>Praesent libero</li>         <li>Fusce nec tellus sed augue semper porta</li>         <li>Mauris massa</li>         <li>Vestibulum lacinia arcu eget nulla</li>     </ul>      <p>Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>In hac habitasse platea dictumst. Maecenas faucibus mollis interdum. Sed posuere consectetur est at lobortis.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p> </body> </html>', ' <html lang="en">  <body>     <h1>Lorem Ipsum Test Document</h1>     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>          <h2>Key Points</h2>     <ul>         <li>Lorem ipsum dolor sit amet</li>         <li>Consectetur adipiscing elit</li>         <li>Sed do eiusmod tempor incididunt</li>         <li>Ut labore et dolore magna aliqua</li>         <li>Ut enim ad minim veniam</li>     </ul>      <p>Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc.</p>      <h2>Additional Information</h2>     <ul>         <li>Proin sagittis nisl rhoncus mattis</li>         <li>Praesent libero</li>         <li>Fusce nec tellus sed augue semper porta</li>         <li>Mauris massa</li>         <li>Vestibulum lacinia arcu eget nulla</li>     </ul>      <p>Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>In hac habitasse platea dictumst. Maecenas faucibus mollis interdum. Sed posuere consectetur est at lobortis.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p> </body> </html>', ' <html lang="en">  <body>     <h1>Lorem Ipsum Test Document</h1>     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>          <h2>Key Points</h2>     <ul>         <li>Lorem ipsum dolor sit amet</li>         <li>Consectetur adipiscing elit</li>         <li>Sed do eiusmod tempor incididunt</li>         <li>Ut labore et dolore magna aliqua</li>         <li>Ut enim ad minim veniam</li>     </ul>      <p>Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc.</p>      <h2>Additional Information</h2>     <ul>         <li>Proin sagittis nisl rhoncus mattis</li>         <li>Praesent libero</li>         <li>Fusce nec tellus sed augue semper porta</li>         <li>Mauris massa</li>         <li>Vestibulum lacinia arcu eget nulla</li>     </ul>      <p>Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>In hac habitasse platea dictumst. Maecenas faucibus mollis interdum. Sed posuere consectetur est at lobortis.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p> </body> </html>', '<p> policies </p>', ' <html lang="en">  <body>     <h1>Lorem Ipsum Test Document</h1>     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>          <h2>Key Points</h2>     <ul>         <li>Lorem ipsum dolor sit amet</li>         <li>Consectetur adipiscing elit</li>         <li>Sed do eiusmod tempor incididunt</li>         <li>Ut labore et dolore magna aliqua</li>         <li>Ut enim ad minim veniam</li>     </ul>      <p>Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc.</p>      <h2>Additional Information</h2>     <ul>         <li>Proin sagittis nisl rhoncus mattis</li>         <li>Praesent libero</li>         <li>Fusce nec tellus sed augue semper porta</li>         <li>Mauris massa</li>         <li>Vestibulum lacinia arcu eget nulla</li>     </ul>      <p>Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue. Aenean lacinia bibendum nulla sed consectetur.</p>      <p>In hac habitasse platea dictumst. Maecenas faucibus mollis interdum. Sed posuere consectetur est at lobortis.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p>      <p>Donec ullamcorper nulla non metus auctor fringilla. Nulla vitae elit libero, a pharetra augue.</p>      <p>Vestibulum id ligula porta felis euismod semper. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>      <p>Curabitur blandit tempus porttitor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.</p>      <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna, vel scelerisque nisl consectetur et.</p> </body> </html>');


--
-- Data for Name: product_groups; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (1, '2025-04-23 21:05:23.825715', NULL, 'other');
INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (2, '2025-05-14 02:10:49.978783', NULL, 'AI');
INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (3, '2025-05-14 02:10:52.333377', NULL, 'Windows');
INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (4, '2025-05-14 02:10:54.684567', NULL, 'office');
INSERT INTO public.product_groups (id, created_at, deleted_at, name) VALUES (5, '2025-05-14 02:10:56.173013', NULL, 'gift card');


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (14, '2025-05-11 17:06:00.69428', NULL, 1, NULL, 'Youtube', 12.99, 9.99, 13, 0, true, 'youtube', '["digital", "music", "streaming"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (16, '2025-05-12 21:32:20.404573', NULL, 1, NULL, 'product phong', 12.99, 9.99, 15, 0, true, 'product-phong', '["digital", "music", "streaming"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (19, '2025-05-12 23:37:51.33737', NULL, 1, NULL, 'Spotify Premium', 12.99, 9.99, 18, 0, true, 'spotify-premium', '["digital"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (20, '2025-05-13 00:13:31.037736', NULL, 1, 'https://res.cloudinary.com/dm45tt6nt/image/upload/v1747070010/ecommerce/product/8ea092fb-c495-4f43-b7cb-26c5ef20b8a9.png', 'aaaa', 10000.00, 9800.00, 19, 0, true, 'a', '["abc"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (17, '2025-05-12 21:36:49.037095', '2025-05-13 10:13:16.594586', 1, NULL, '21520507', 0.00, 0.00, 16, 0, true, 'as', '["sds"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (18, '2025-05-12 21:51:07.058552', '2025-05-13 10:13:16.594586', 1, NULL, '21520507', 0.00, 0.00, 17, 0, true, '21520507', '["sds"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (9, '2025-04-23 21:09:11.903325', NULL, 1, NULL, 'thanh phong', 1991.00, 99.00, 2, 5, false, '', '[]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (10, '2025-04-24 11:48:34.205537', NULL, 1, NULL, 'youtube abc', 12.99, 9.99, 1, 2, true, NULL, '["oke", "mytag"]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (8, '2025-04-17 16:49:31.057025', NULL, NULL, NULL, 'the jakarta product', 1000.00, 50.00, 3, 6, false, 'the-jakarta-product', '[]');
INSERT INTO public.products (id, created_at, deleted_at, group_id, image_url_id, name, original_price, price, prod_desc_id, quantity, represent, slug, tags) VALUES (22, '2025-05-24 12:23:53.166715', NULL, 1, NULL, 'product with long desc', 12.99, 9.99, 21, 0, true, 'product-with-long-desc', '["digital", "music", "streaming"]');


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (3, '2025-04-25 16:23:52.76518', NULL, 4, '123', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (5, '2025-05-16 22:13:31.586052', NULL, 4, 'asdfasdfasdfadsf', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (6, '2025-05-19 13:53:50.004373', NULL, 4, 'hi my comment', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (2, '2025-04-25 16:21:07.625201', '2025-05-20 08:42:36.655472', 1, 'string', NULL, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (4, '2025-04-25 16:40:36.213105', '2025-05-20 08:42:36.655472', 4, '123', 2, 9);
INSERT INTO public.comments (id, created_at, deleted_at, author_id, content, parent_comment_id, product_id) VALUES (7, '2025-05-19 13:59:31.536416', '2025-05-20 09:45:30.335485', 4, 'my another comment', NULL, 8);


--
-- Data for Name: coupons; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.coupons (id, created_at, deleted_at, available_from, available_to, code, current_usage, description, max_applied_amount, min_amount, type, usage_limit, value) VALUES (1, '2025-04-17 22:25:17.232006', NULL, '2025-04-17', '2025-04-17', 'PHONG', 0, 'string', 20000.00, 156000.00, 'PERCENTAGE', 1, 12.00);


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

INSERT INTO public.product_favorites (created_at, product_id, profile_id) VALUES ('2025-05-19 13:14:04.77749', 8, 4);
INSERT INTO public.product_favorites (created_at, product_id, profile_id) VALUES ('2025-05-19 13:14:04.77749', 9, 3);
INSERT INTO public.product_favorites (created_at, product_id, profile_id) VALUES ('2025-05-19 13:44:14.225414', 14, 3);
INSERT INTO public.product_favorites (created_at, product_id, profile_id) VALUES ('2025-05-20 09:33:56.144749', 17, 4);


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
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (67, '2025-05-15 21:42:43.779527', 8, 'S1', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (69, '2025-05-23 15:29:48.961988', 8, 'ABCDE123', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (70, '2025-05-23 15:29:48.961988', 9, 'AAAAA', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (68, '2025-05-23 15:29:48.961988', 8, '1232', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (71, '2025-05-23 22:55:46.448712', 10, 'OK', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (72, '2025-05-23 22:56:34.169802', 10, 'OK2', 'US');
INSERT INTO public.product_items (id, created_at, product_id, product_key, region) VALUES (73, '2025-05-24 12:09:23.709951', 8, 'DEL1', 'US');


--
-- Data for Name: product_items_used; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (1, '2025-05-11 22:09:29.323963', NULL, 14, 'Y1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (2, '2025-05-11 22:09:29.323963', NULL, 14, 'Y2', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (3, '2025-05-11 22:47:56.497701', NULL, 14, 'Y4', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (4, '2025-05-11 22:50:24.539057', NULL, 14, 'Y5', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (5, '2025-05-11 22:51:46.131726', NULL, 14, 'Y6', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (6, '2025-05-23 15:27:31.728066', NULL, 8, 'Del1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (7, '2025-05-23 15:27:31.728066', NULL, 8, 'Del1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (8, '2025-05-23 15:27:31.728066', NULL, 9, 'Del2', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (9, '2025-05-24 12:10:26.519807', NULL, 8, 'DEL1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (10, '2025-05-24 12:10:29.508723', NULL, 8, 'DEL1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (11, '2025-05-24 12:12:30.823749', NULL, 8, 'DEL1', 'US');
INSERT INTO public.product_items_used (id, created_at, order_detail_id, product_id, product_key, region) VALUES (12, '2025-05-24 12:13:27.479572', NULL, 8, 'DEL1', 'US');


--
-- Data for Name: products_categories; Type: TABLE DATA; Schema: public; Owner: phong
--

INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 8);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 10);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 9);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 14);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 14);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 16);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 17);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 18);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 19);
INSERT INTO public.products_categories (category_id, product_id) VALUES (2, 19);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 20);
INSERT INTO public.products_categories (category_id, product_id) VALUES (1, 22);
INSERT INTO public.products_categories (category_id, product_id) VALUES (3, 22);
INSERT INTO public.products_categories (category_id, product_id) VALUES (5, 22);


--
-- Name: accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.accounts_id_seq', 9, true);


--
-- Name: blogs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.blogs_id_seq', 6, true);


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.categories_id_seq', 7, true);


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.comments_id_seq', 7, true);


--
-- Name: coupons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.coupons_id_seq', 1, true);


--
-- Name: genre1_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.genre1_id_seq', 4, true);


--
-- Name: genre2_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.genre2_id_seq', 13, true);


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

SELECT pg_catalog.setval('public.product_description_id_seq', 21, true);


--
-- Name: product_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_groups_id_seq', 5, true);


--
-- Name: product_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_items_id_seq', 73, true);


--
-- Name: product_items_used_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.product_items_used_id_seq', 12, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.products_id_seq', 22, true);


--
-- Name: profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: phong
--

SELECT pg_catalog.setval('public.profiles_id_seq', 7, true);


--
-- PostgreSQL database dump complete
--

