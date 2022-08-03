--liquibase formatted sql

--changeset khlystov:2022-08-04-change-size-of-idempotence_uid-field-uid
alter table yatt.idempotence_uid
    alter column uid type varchar(100);