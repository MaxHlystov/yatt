--liquibase formatted sql

--changeset khlystov:2022-07-31-add-user-table
create table if not exists yatt.user
(
    id   bigserial primary key,
    name text not null
);

--changeset khlystov:2022-08-01-add-users
insert into yatt.user (name)
values ('max'),
       ('admin'),
       ('programmer');