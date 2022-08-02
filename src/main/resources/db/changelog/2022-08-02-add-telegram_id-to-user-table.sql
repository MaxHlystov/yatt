--liquibase formatted sql

--changeset khlystov:2022-08-02-add-telegram_id-to-user-table
alter table yatt.user
    add column telegram_id bigint;
