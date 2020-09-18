create table atlas_user(
    "id" bigserial UNIQUE,
    "username" varchar not null,
    "email" varchar not null,
    "password" varchar not null
)


