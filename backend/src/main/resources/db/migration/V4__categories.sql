create table if not exists categories (
    id bigserial primary key,
    user_id bigint not null,
    name varchar(80) not null,
    type varchar(10) not null, -- INCOME | EXPENSE
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_categories_user foreign key (user_id) references users (id),
    constraint uq_categories_user_name_type unique (user_id, name, type)
);

create index if not exists idx_categories_user_id on categories (user_id);