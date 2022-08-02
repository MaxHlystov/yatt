--liquibase formatted sql

--changeset khlystov:2022-07-31-add-status-table
create table if not exists yatt.status
(
    id          bigserial primary key,
    name        varchar(100) not null,
    description text         not null
);

--changeset khlystov:2022-07-31-add-values-to-status-table
insert into yatt.status (id, name, description)
values (1, 'Backlog', 'Запланировано'),
       (2, 'NeedInfo', 'Нужна информация'),
       (3, 'InWork', 'В работе'),
       (4, 'Review', 'На проверке'),
       (5, 'Solved', 'Решено'),
       (6, 'Closed', 'Закрыто')
;
