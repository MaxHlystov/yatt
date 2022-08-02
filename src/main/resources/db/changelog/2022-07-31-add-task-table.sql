--liquibase formatted sql

--changeset khlystov:2022-07-31-add-task-table
create table if not exists yatt.task
(
    id          bigserial primary key,
    status_id      bigint       not null references yatt.status (id)
        on delete restrict,
    name        varchar(255) not null,
    description text,
    created_at  timestamp    not null default now(),
    created_by  bigint       not null references yatt.user (id)
        on delete restrict,
    assignee_id    bigint       references yatt.user (id)
                                 on delete set null,
    modified_at timestamp    not null default now()
);
