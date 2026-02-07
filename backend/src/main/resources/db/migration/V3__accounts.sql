create table if not exists accounts (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  name varchar(80) not null,
  type varchar(20) not null,
  initial_balance_cents bigint not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);

create index if not exists idx_accounts_user_id on accounts(user_id);
