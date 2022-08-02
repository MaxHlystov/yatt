--liquibase formatted sql

--changeset khlystov:2022-08-01-add-create_task_uid-table
create table if not exists yatt.create_task_uid
(
    uid varchar(36) primary key,
    created_at timestamp not null default now()
);
