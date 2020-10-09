alter table project
    add is_private boolean not null default false;
alter table project
    add img text not null default '../../../assets/images/icon-business-pack/svg/101-laptop.svg'::text;

create table role_in_project
(
    id serial primary key ,
    role varchar not null
);

insert into role_in_project (role) values ('VISITOR');
insert into role_in_project (role) values ('LEAD');
insert into role_in_project (role) values ('COLLABORATOR');

create table project_role_member
(
    project_id uuid not null ,
    role_id int not null ,
    member_id varchar not null ,

    constraint project_role_member_pkey primary key (project_id, role_id, member_id),

    constraint fk_project_role_member_project_id
        foreign key (project_id)
        references project(project_id),

    constraint fk_project_role_member_role_id
        foreign key (role_id)
            references role_in_project(id)
);