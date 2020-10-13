create table if not exists atlas_errors
(
    id int not null,
    code varchar(5) not null default ('ATLAS'),
    title varchar(250) not null ,
    description text,

    constraint atlas_errors_pkey primary key (code, id)
);


insert into atlas_errors (id, code, title, description) values (1, 'ATLAS', 'Project with this key already exist in your organization.', null);
insert into atlas_errors (id, code, title, description) values (2, 'ATLAS', 'Organization with this name already exists.', null);
insert into atlas_errors (id, code, title, description) values (3, 'ATLAS', 'Couldn''t find ''organization'' with id - + $organizationId$', 'Instead of $organizationId$, you need to substitute a variable containing the requested id');
insert into atlas_errors (id, code, title, description) values (4, 'ATLAS', 'Invalid project type - %s', null);
insert into atlas_errors (id, code, title, description) values (5, 'ATLAS', 'Invalid project typeId - %d', null);
insert into atlas_errors (id, code, title, description) values (6, 'ATLAS', 'Invalid project id - %d', null);
