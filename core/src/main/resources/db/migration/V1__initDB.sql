CREATE EXTENSION pgcrypto;

create table atlas_user(
    "user_id" UUID  primary key unique default gen_random_uuid(),
    "sub" varchar(255) not null,
    "email" varchar(255) not null,
    "last_modify" timestamp with time zone default current_timestamp
)


