create table if not exists organization
(
    organization_id UUID default gen_random_uuid() not null
        constraint organization_pkey primary key,
    name  varchar(100) unique not null,
    valid_name varchar(100) unique not null,
    owner_user_id varchar not null,
    last_modify timestamp with time zone default current_timestamp,
    img text not null default '../../../assets/images/icon-business-pack/svg/101-laptop.svg'::text
);

create table if not exists org_role
(
    org_role_id int unique not null
        constraint opt_pkey primary key,
    role varchar(25)
);

insert into org_role (org_role_id, role) values (1, 'OWNER');
insert into org_role (org_role_id, role) values (2, 'MANAGER');
insert into org_role (org_role_id, role) values (3, 'MEMBER');

create table if not exists org_role_member
(
    organization_id uuid,
    member_id varchar,
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
    pt_id int unique not null
        constraint project_type_pkey primary key,
    type varchar(25)
);

insert into project_type (pt_id, type) values (1, 'SCRUM');
insert into project_type (pt_id, type) values (2, 'KANBAN');

create table if not exists project
(
    project_id UUID default gen_random_uuid() unique not null,
    name  varchar(100)  not null,
    key  varchar(5)  not null,
    project_type_id  int  not null,
    organization_id uuid not null,
    lead_user_id varchar not null,
    is_private boolean not null default false,
    img text not null default '../../../assets/images/icon-business-pack/svg/101-laptop.svg',
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
    id int unique not null,
    code varchar(5) not null default ('ATLAS'),
    title varchar(250) not null ,
    description text,

    constraint atlas_errors_pkey primary key (code, id)
);

