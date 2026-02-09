create table if not exists transactions (
    id bigserial primary key,
    user_id bigint not null,
    account_id bigint not null,
    category_id bigint,
    type varchar(10) not null, -- INCOME | EXPENSE
    amount_cents bigint not null check (amount_cents > 0),
    description varchar(160),
    occurred_at timestamp not null, -- data da transação
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_transactions_user foreign key (user_id) references users (id),
    constraint fk_transactions_account foreign key (account_id) references accounts (id),
    constraint fk_transactions_category foreign key (category_id) references categories (id)
);

create index if not exists idx_transactions_user_id on transactions (user_id);

create index if not exists idx_transactions_account_id on transactions (account_id);

create index if not exists idx_transactions_occurred_at on transactions (occurred_at);