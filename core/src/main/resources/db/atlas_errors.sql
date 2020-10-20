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
insert into atlas_errors (id, code, title, description) values (3, 'ATLAS', 'Could not find organization with id - %s', 'Instead of %s, you need to substitute a variable containing the requested id');
insert into atlas_errors (id, code, title, description) values (4, 'ATLAS', 'Invalid project type - %s', null);
insert into atlas_errors (id, code, title, description) values (5, 'ATLAS', 'Invalid project typeId - %d', null);
insert into atlas_errors (id, code, title, description) values (6, 'ATLAS', 'Invalid project id - %d', null);
insert into atlas_errors (id, code, title, description) values (7, 'ATLAS', 'Invalid organization id - %s', 'Instead of %d, you need to substitute incoming organization id');
insert into atlas_errors (id, code, title, description) values (8, 'ATLAS', 'Owner ids does not match: expected %s, was %2$s', 'Instead of %s, you need to substitute saved owner id, %2$s - incoming owner id');
insert into atlas_errors (id, code, title, description) values (9, 'ATLAS', 'Member with id %s is already exists in organization %2$s', 'Instead of %s, you need to substitute member id, %2$s - organization id');
insert into atlas_errors (id, code, title, description) values (10, 'ATLAS', 'Invalid organization member role id - %d', 'Instead of %d, you need to substitute user role id');
insert into atlas_errors (id, code, title, description) values (11, 'ATLAS', 'Invalid organization member role - %s', 'Instead of %d, you need to substitute user role');

