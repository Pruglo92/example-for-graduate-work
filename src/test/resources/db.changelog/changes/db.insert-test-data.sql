INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('User1FirstName', 'User1LastName', 'user1@gmail.com', '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-01', 'USER');

INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('User2FirstName', 'User2LastName', 'user2@gmail.com', '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-02', 'USER');

INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('AdminFirstName', 'AdminLastName', 'admin@gmail.com', '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-03', 'ADMIN');

INSERT INTO user_images (file_name, file_path, file_size, media_type)
VALUES ('user1-avatar.jpg', '/images/user1-avatar.jpg', 42361, 'image/jpeg');

INSERT INTO user_images (file_name, file_path, file_size, media_type)
VALUES ('user2-avatar.png', '/images/user2-avatar.png', 59978, 'image/png');

UPDATE users SET user_image_id = 1 WHERE id = 1;
UPDATE users SET user_image_id = 2 WHERE id = 2; 

INSERT INTO ads (description, price, title, user_id)
VALUES ('Ad1Description', 100, 'Ad1Title', 1);

INSERT INTO ads (description, price, title, user_id)
VALUES ('Ad2Description', 150, 'Ad2Title', 2);

INSERT INTO ad_images (file_name, file_path, file_size, media_type)
VALUES ('ad1-image.png', '/images/ad1-image.png', 2327754, 'image/png');

INSERT INTO ad_images (file_name, file_path, file_size, media_type)
VALUES ('ad2-image.png', '/images/ad2-image.png', 1043762, 'image/png');

UPDATE ads SET image_id = 1 WHERE id = 1;
UPDATE ads SET image_id = 2 WHERE id = 2;

INSERT INTO comments (text, ad_id, user_id)
VALUES ('Comment1 by Ad1 Author', 1, 1);

INSERT INTO comments (text, ad_id, user_id)
VALUES ('Comment2 by Another User', 1, 2);

INSERT INTO comments (text, ad_id, user_id)
VALUES ('Comment3 by Another User', 2, 1);

INSERT INTO comments (text, ad_id, user_id)
VALUES ('Comment4 by Ad2 Author', 2, 2); 