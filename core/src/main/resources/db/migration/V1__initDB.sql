create table if not exists organization
(
    organization_id UUID default gen_random_uuid() not null
        constraint organization_pkey primary key,
    name  varchar(100) unique not null,
    valid_name varchar(100) unique not null,
    owner_user_id varchar not null,
    last_modify timestamp with time zone default current_timestamp
);

create table if not exists org_role
(
    org_role_id serial
        constraint opt_pkey primary key,
    role varchar(25)
);

insert into org_role (role) values ('OWNER');
insert into org_role (role) values ('MANAGER');
insert into org_role (role) values ('MEMBER');

create table if not exists org_user_role
(
    organization_id uuid,
    atlas_user_id varchar,
    org_role_id int,

    constraint fk_org_user_role_organization_id
        foreign key (organization_id)
            references organization(organization_id) on delete cascade ,
    constraint fk_org_user_role_org_role_id
        foreign key (org_role_id)
            references org_role(org_role_id)
);

/*Project*/

create table if not exists project_type
(
    pt_id serial
        constraint project_type_pkey primary key,
    type varchar(25)
);

insert into project_type (type) values ('SCRUM');
insert into project_type (type) values ('KANBAN');

create table if not exists project
(
    project_id UUID default gen_random_uuid() not null,
    name  varchar(100)  not null,
    key  varchar(5)  not null,
    project_type_id  int  not null,
    organization_id uuid not null,
    lead_user_id varchar not null,
    last_modify timestamp with time zone default current_timestamp,

    constraint project_pkey primary key (organization_id, key),

    constraint fk_project_pt_id
        foreign key (project_type_id)
            references project_type(pt_id),

    constraint fk_project_organization_id
        foreign key (organization_id)
            references organization(organization_id)
);

/*errors codes*/

create table if not exists atlas_errors
(
    id int not null,
    code varchar(5) not null default ('ATLAS'),
    title varchar(250) not null ,
    description text,

    constraint atlas_errors_pkey primary key (code, id)
);
insert into atlas_errors (id, title) values ( 1, 'Project with this key already exist in your organization.');
insert into atlas_errors (id, title) values ( 2, 'Organization with this name already exists.');
insert into atlas_errors (id, title, description) values ( 3, 'Couldn''t find ''organization'' with id - + $organizationId$', 'Instead of $organizationId$, you need to substitute a variable containing the requested id');



