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
            references role_in_project(id)
);
create index not_unique_index_project_role on project_role_member (project_id, role_id);