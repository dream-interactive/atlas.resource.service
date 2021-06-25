create function last_modify() returns trigger
as $lm$
    begin
        NEW.last_modify = current_timestamp;
        RETURN NEW;
    end;
$lm$ LANGUAGE plpgsql;

create table if not exists user_profile
(
    sub_user_id varchar(255) unique not null,
    email varchar not null,
    email_verified boolean not null,
    family_name varchar not null,
    given_name varchar not null,
    name  varchar(255)  not null,
    local varchar not null default 'en',
    picture  text  not null,
    last_modify timestamp with time zone not null default current_timestamp,

    constraint user_profile_pkey primary key (sub_user_id)
);
create trigger user_profile_last_modify
    after update on user_profile execute procedure last_modify();

create table if not exists organization
(
    organization_id UUID default gen_random_uuid() not null
        constraint organization_pkey primary key,
    name  varchar(100) unique not null,
    valid_name varchar(100) unique not null,
    owner_user_id varchar not null,
    last_modify timestamp with time zone default current_timestamp,
    img text not null default '../../../assets/images/icon-business-pack/svg/101-laptop.svg'::text,

    constraint fk_user_profile_sub_user_id_organization_owner_user_id
        foreign key (owner_user_id)
            references user_profile(sub_user_id) on delete cascade
);

create trigger organization_last_modify
    after update on organization execute procedure last_modify();

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
            references org_role(org_role_id),
    constraint fk_user_profile_sub_user_id_org_role_member_owner_member_id
        foreign key (member_id)
            references user_profile(sub_user_id) on delete cascade


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
    project_id UUID default gen_random_uuid() unique not null primary key ,
    name  varchar(100)  not null,
    key  varchar(5)  not null,
    project_type_id  int  not null,
    organization_id uuid not null,
    lead_user_id varchar not null,
    is_private boolean not null default false,
    img text not null default '../../../assets/images/icon-business-pack/svg/101-laptop.svg',
    last_modify timestamp with time zone default current_timestamp,

    constraint UC_project_orgAndKey unique (organization_id, key),

    constraint fk_project_pt_id
        foreign key (project_type_id)
            references project_type(pt_id),

    constraint fk_project_organization_id
        foreign key (organization_id)
            references organization(organization_id),
    constraint fk_user_profile_sub_user_id_project_lead_user_id
        foreign key (lead_user_id)
            references user_profile(sub_user_id) on delete cascade
);
create trigger project_last_modify
    after update on project execute procedure last_modify();

create table role_in_project
(
    id int unique not null primary key ,
    role varchar not null
);

insert into role_in_project (id, role) values (1, 'VISITOR');
insert into role_in_project (id, role) values (2, 'LEAD');
insert into role_in_project (id, role) values (3, 'COLLABORATOR');

create table project_role_member
(
    project_id uuid not null ,
    role_id int not null ,
    member_id varchar not null ,

    constraint project_role_member_pkey primary key (project_id, member_id),

    constraint fk_project_role_member_project_id
        foreign key (project_id)
            references project(project_id),

    constraint fk_project_role_member_role_id
        foreign key (role_id)
            references role_in_project(id),
    constraint fk_user_profile_sub_user_id_project_role_member_member_id
        foreign key (member_id)
            references user_profile(sub_user_id) on delete cascade
);
create index not_unique_index_project_role on project_role_member (project_id, role_id);

