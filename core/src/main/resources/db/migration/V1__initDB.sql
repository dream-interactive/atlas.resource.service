create table if not exists organization
(
    organization_id UUID default gen_random_uuid() not null
        constraint organization_pkey primary key,
    name varchar(100) not null,
    owner_user_id varchar not null,
    last_modify timestamp with time zone default current_timestamp
);

create table if not exists org_permission_type
(
    opt_id serial
        constraint opt_pkey primary key,
    type varchar(25)
);

create table if not exists org_user_permission
(
    organization_id uuid,
    atlas_user_id varchar,
    opt_id int,/* org_permission_type id*/

    constraint fk_org_id_org_user_permission
        foreign key (organization_id)
            references organization(organization_id) on delete cascade ,
    constraint fk_opt_id_org_user_permission
        foreign key (opt_id)
            references org_permission_type(opt_id)
)


