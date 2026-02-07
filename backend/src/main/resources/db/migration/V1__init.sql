create table if not exists users (
  id bigserial primary key,
  name varchar(120) not null,
  email varchar(180) not null unique,
  password_hash varchar(255) not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);
