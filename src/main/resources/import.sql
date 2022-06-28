INSERT INTO role (id, name) VALUES(1, 0);
INSERT INTO role (id, name) VALUES (2,1);

INSERT INTO category(id, created_at, is_deleted, created_by, updated_at, title, description, url_image, count_course, count_user)
VALUES (1,now(),false, 'SYSTEM', now(), 'Web Development' , 'Course yang mempelajari proses pembangunan dan pemeliharaan website. Mulai dari pembuatan website berisi teks sederhana hingga yang bentuknya kompleks seperti platform sosmed atau web app' , 'https://capstone-lms-storage.s3.amazonaws.com/category-images/cc536b1f7ff24466beaf504044c23bbd/python.png', 0, 0);

INSERT INTO course(id, created_at, is_deleted, created_by, updated_at, category_id, title, description, rating, url_image)
VALUES (1, now(), false, 'SYSTEM', now(), 1, 'Javascript for Beginners', 'Pelajari Javascript dan tingkatkan kemampuan desain web Anda dengan Course Javascript untuk pemula ini.', 0.0, 'https://capstone-lms-storage.s3.amazonaws.com/course-images/cc536b1f7ff24466beaf504044c23bbd/python.png'),
(2, now(), false, 'SYSTEM', now(), 1, 'HTML for Beginners', 'Pelajari HTML dan tingkatkan kemampuan desain web Anda dengan Course HTML untuk pemula ini.', 0.0, 'https://capstone-lms-storage.s3.amazonaws.com/course-images/cc536b1f7ff24466beaf504044c23bbd/python.png');

