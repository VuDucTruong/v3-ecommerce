
INSERT INTO public.product_groups (deleted_at, name)
VALUES (NULL, 'AI');
INSERT INTO public.product_groups (deleted_at, name)
VALUES (NULL, 'Windows');
INSERT INTO public.product_groups (deleted_at, name)
VALUES (NULL, 'office');
INSERT INTO public.product_groups (deleted_at, name)
VALUES (NULL, 'gift card');


INSERT INTO public.genre1 (deleted_at, name)
VALUES (NULL, 'Mẹo hay');
INSERT INTO public.genre1 (deleted_at, name)
VALUES (NULL, 'AI');
INSERT INTO public.genre1 (deleted_at, name)
VALUES (NULL, 'Movies');


INSERT INTO public.genre2 (genre1_id, name)
VALUES (1, 'Mẹo chơi game');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (1, 'Tin tức game');

INSERT INTO public.genre2 (genre1_id, name)
VALUES (2, 'Điện thoại');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (2, 'Ứng dụng');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (2, 'Máy tính');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (2, 'Phần cứng');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (2, 'Thủ thuật');

INSERT INTO public.genre2 (genre1_id, name)
VALUES (4, 'Đánh giá');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (4, 'Giới thiệu phim');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (4, 'Netflix');
INSERT INTO public.genre2 (genre1_id, name)
VALUES (4, 'Phim chiếu rạp');


