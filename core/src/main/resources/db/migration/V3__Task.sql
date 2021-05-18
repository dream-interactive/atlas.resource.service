create table if not exists tasks_container
(
    idtc           bigserial unique not null,
    name           varchar(50)      not null,
    idp            uuid             not null,

    index_number   int              not null, /* for saving order place */
    can_be_deleted boolean default true,

    constraint tasks_container_pkey primary key (idtc),

    constraint fk_project_id_task_idp
        foreign key (idp)
            references project (project_id) on delete cascade

);


create table project_custom_fields_types
(
    id   serial unique not null primary key,
    type varchar       not null unique
);
insert into project_custom_fields_types(type)
values ('STRING');
insert into project_custom_fields_types(type)
values ('NUMBER');
insert into project_custom_fields_types(type)
values ('DATETIME');
insert into project_custom_fields_types(type)
values ('BOOLEAN');

create table project_custom_fields
(
    idp_cf        bigserial unique not null primary key,
    project_id    UUID             not null,
    field_name    varchar(255)     not null,
    field_type_id int              not null,
    description   text,
    index_number  int              not null,
    main          boolean          not null,

    constraint fk_project_custom_fields_field_type_id_project_custom_fields_id
        foreign key (field_type_id)
            references project_custom_fields_types (id) on delete cascade
);


/*
    History of changes in the task.
    See function history()
*/
create table task_history
(
    id_history  bigserial unique not null,
    row_history jsonb            not null, /* old row from task table*/

    constraint history_pkey primary key (id_history)
);

create table task_history_archive
(
    id_history  bigint unique not null,
    row_history jsonb         not null,

    constraint task_history_archive_pkey primary key (id_history)
);

create function history() returns trigger
as
$history$
declare
    tbl_name varchar;
begin
    tbl_name := TG_ARGV[0];
    execute 'insert into ' || tbl_name || '(row_history) values (' || row_to_json(old) || ');';
    return new;
end;
$history$ LANGUAGE plpgsql;

create function task_creating() returns trigger
as
$history$
declare
    i int;
begin
    select max(key_number) into i from task where project_id = new.project_id;

    if i is NULL then
        new.key_number := 0;
    else
        new.key_number := i + 1;
    end if;
    return new;
end;
$history$ LANGUAGE plpgsql;


create table task
(
    idt                bigserial unique not null, /* task id */
    idtc               bigint           not null, /* task_container id */
    index_number       int              not null, /* task index in container for saving order place*/
    summary            varchar          not null,
    project_id         uuid             not null,
    key_number         int              not null, /* key number */
    assign_to_user_id  varchar,
    creator_id         varchar          not null,
    checker_id         varchar,
    priority           varchar          not null default 'Medium',
    description        text,
    points             smallint,
    close_before_tasks bigint[]         not null default array[]::bigint[],
    close_after_tasks  bigint[]         not null default array[]::bigint[],
    close_with_tasks   bigint[]         not null default array[]::bigint[],
    labels             varchar[]        not null default array[]::varchar[],
    date_time_s        timestamp        not null default current_timestamp, /* start date */
    date_time_e        timestamp, /* end date */
    last_modify        timestamp,


    custom_fields      jsonb,

    constraint UC_task_projectAndKey unique (project_id, key_number),

    constraint task_pkey primary key (idt),

    constraint fk_user_profile_sub_user_id_task_creator_id
        foreign key (creator_id)
            references user_profile (sub_user_id),

    constraint fk_user_profile_sub_user_id_task_assign_to_user_id
        foreign key (assign_to_user_id)
            references user_profile (sub_user_id),

    constraint fk_user_profile_sub_user_id_task_checker_id
        foreign key (checker_id)
            references user_profile (sub_user_id),

    constraint fk_project_project_id_task_project_id
        foreign key (project_id)
            references project (project_id)

);

create trigger task_last_modify
    after update
    on task
execute procedure last_modify();

create trigger task_history
    before update
    on task
execute procedure history('task_history');

create trigger task_creating
    before insert
    on task
    for each row
execute procedure task_creating();


/* see task table */
create table if not exists tasks_archive
(
    idt                   bigint unique not null,
    idtc                   bigint        not null,
    index_number           int           not null,
    summary                varchar       not null,
    project_id             uuid          not null,
    key_number             int           not null,
    assign_to_user_id      varchar,
    creator_id             varchar,
    checker_id             varchar,
    priority               varchar       not null default 'Medium',
    description            text,
    points                 smallint,
    close_before_tasks     bigint[]      not null default array[]::bigint[],
    close_after_tasks      bigint[]      not null default array[]::bigint[],
    close_with_tasks       bigint[]      not null default array[]::bigint[],
    labels                 varchar[]     not null default array[]::varchar[],
    date_time_s            timestamp     not null default current_timestamp,
    date_time_assumption_e timestamp, /* assumption end date*/
    date_time_e            timestamp,
    last_modify            timestamp,

    custom_fields          jsonb,

    constraint UC_tasks_archive_projectAndKey unique (project_id, key_number),

    constraint tasks_archive_pkey primary key (idt),

    constraint fk_up_sub_user_id_tasks_archive_creator_id
        foreign key (creator_id)
            references user_profile (sub_user_id),

    constraint fk_up_sub_user_id_tasks_archive_assign_to_user_id
        foreign key (assign_to_user_id)
            references user_profile (sub_user_id),

    constraint fk_up_sub_user_id_tasks_archive_checker_id
        foreign key (checker_id)
            references user_profile (sub_user_id),

    constraint fk_tasks_archive_container_idtc_task_idtc
        foreign key (idtc)
            references tasks_container (idtc),

    constraint fk_project_project_id_tasks_archive_project_id
        foreign key (project_id)
            references project (project_id)
);


create table if not exists task_comments
(
    idc       bigserial unique not null,
    idt       bigint           not null,
    comment   jsonb            not null,
    is_edited boolean          not null default 'false',
    author_id varchar          not null,

    constraint task_comments_pkey primary key (idc),

    constraint fk_task_idt_task_comments_idt
        foreign key (idt)
            references task (idt) on delete cascade,

    constraint fk_user_profile_sub_user_id_task_comments_author_id
        foreign key (author_id)
            references user_profile (sub_user_id)
)