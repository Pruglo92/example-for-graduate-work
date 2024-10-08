--liquibase formatted sql

--changeset pruglo-ve:20231025-1 failOnError:true
--comment: Create ad_images table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'ad_images';
create table if not exists ad_images
(
    id         integer generated by default as identity
        primary key,
    file_name  varchar(255),
    file_path  varchar(255)         not null,
    file_size  bigint,
    media_type varchar(255)
);

--changeset pruglo-ve:20231025-2 failOnError:true
--comment: Create user_images table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'user_images';
create table if not exists user_images
(
    id         integer generated by default as identity
        primary key,
    file_name  varchar(255),
    file_path  varchar(255)         not null,
    file_size  bigint,
    media_type varchar(255)
);

--changeset pruglo-ve:20231025-3 failOnError:true
--comment: Create users table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'users';
create table if not exists users
(
    id            integer generated by default as identity
        primary key,
    first_name    varchar(255)      not null,
    last_name     varchar(255)      not null,
    login         varchar(255)      not null        unique,
    password      varchar(255)      not null,
    phone         varchar(255)      not null,
    role          varchar(255)      not null,
    user_image_id integer
        constraint fk_users_user_images references user_images(id)
);

--changeset pruglo-ve:20231025-4 failOnError:true
--comment: Create ads table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'ads';
create table if not exists ads
(
    id          integer generated by default as identity
        primary key,
    description varchar(255),
    price       integer             not null,
    title       varchar(255)        not null,
    image_id    integer
        constraint fk_ads_ad_images references ad_images(id),
    user_id     integer             not null
        constraint fk_ads_users references users(id)
);

--changeset pruglo-ve:20231025-5 failOnError:true
--comment: Create comments table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'comments';
create table if not exists comments
(
    id         integer generated by default as identity
        primary key,
    created_at timestamp default current_timestamp,
    text       varchar(255)         not null,
    ad_id      integer              not null
        constraint fk_comments_ads references ads(id),
    user_id    integer              not null
        constraint fk_comments_users references users(id)
);