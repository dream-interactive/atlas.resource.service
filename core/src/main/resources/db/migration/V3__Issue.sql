alter table project
    drop constraint project_pkey;
alter table project
    add primary key (project_id);

alter table project
    add constraint UC_project_orgAndKey unique (organization_id, key);

create table if not exists issues_container
(
    idic         bigserial unique not null,
    name         varchar(50)      not null,
    idp          uuid          not null,

    index_number int              not null, /* for saving order place */

    constraint issues_container_pkey primary key (idic),

    constraint fk_project_id_issue_idp
        foreign key (idp)
            references project (project_id) on delete cascade

);


/* ENUM for project_custom_fields_types */
CREATE TYPE project_custom_fields_types AS ENUM ('STRING', 'NUMBER', 'DATETIME', 'BOOLEAN');

create table project_custom_fields
(
    idp_cf      bigserial unique            not null primary key,
    project_id  UUID                        not null,
    field_name  varchar(255)                not null,
    field_type  project_custom_fields_types not null,
    description text
);


/*
    History of changes in the issue.
    See function history()
*/
create table issue_history
(
    id_history  bigserial unique not null,
    row_history jsonb            not null, /* old row from issue table*/

    constraint history_pkey primary key (id_history)
);

create table issue_history_archive
(
    id_history  bigint unique not null,
    row_history jsonb         not null,

    constraint issue_history_archive_pkey primary key (id_history)
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


/* ENUM for issue priority  */
CREATE TYPE issue_priority AS ENUM ('Low', 'Medium', 'High');


create table issue
(
    idi                 bigserial unique not null, /* issue id */
    idic                bigint           not null, /* issue_container id */
    index_number        int              not null, /* issue index in container for saving order place*/
    name                varchar          not null,
    project_id          uuid             not null,
    key_number          int              not null, /* key number */
    assign_to_user_id   varchar,
    creator_id          varchar          not null,
    checker_id          varchar,
    priority            issue_priority   not null default 'Medium',
    status              varchar(50)      not null, /* issue_container name or 'DONE' */
    description         jsonb,
    points              smallint,
    close_before_issues bigint[],
    close_after_issues  bigint[],
    close_with_issues   bigint[],
    labels              varchar[],
    date_time_s         timestamp        not null default current_timestamp, /* start date */
    date_time_e         timestamp, /* end date */
    last_modify         timestamp,

    custom_fields       jsonb,

    constraint UC_issue_projectAndKey unique (project_id, key_number),

    constraint issue_pkey primary key (idi),

    constraint fk_user_profile_sub_user_id_issue_creator_id
        foreign key (creator_id)
            references user_profile (sub_user_id),

    constraint fk_user_profile_sub_user_id_issue_assign_to_user_id
        foreign key (assign_to_user_id)
            references user_profile (sub_user_id),

    constraint fk_user_profile_sub_user_id_issue_checker_id
        foreign key (checker_id)
            references user_profile (sub_user_id),

    constraint fk_project_project_id_issue_project_id
        foreign key (project_id)
            references project (project_id)

);


create trigger issue_last_modify
    after update
    on issue
execute procedure last_modify();

create trigger issue_history
    before update
    on issue
execute procedure history('issue_history');

/* see issue table */
create table if not exists issues_archive
(
    idi                    bigint unique  not null,
    idic                   bigint         not null,
    index_number           int            not null,
    name                   varchar        not null,
    project_id             uuid           not null,
    key_number             int            not null,
    assign_to_user_id      varchar,
    creator_id             varchar,
    checker_id             varchar,
    priority               issue_priority not null default 'Medium',
    status                 varchar(50)    not null,
    description            jsonb,
    points                 smallint,
    close_before_issues    bigint[],
    close_after_issues     bigint[],
    close_with_issues      bigint[],
    labels                 varchar[],
    date_time_s            timestamp      not null default current_timestamp,
    date_time_assumption_e timestamp, /* assumption end date*/
    date_time_e            timestamp,
    last_modify            timestamp,

    custom_fields          jsonb,

    constraint UC_issues_archive_projectAndKey unique (project_id, key_number),

    constraint issues_archive_pkey primary key (idi),

    constraint fk_up_sub_user_id_issues_archive_creator_id
        foreign key (creator_id)
            references user_profile (sub_user_id),

    constraint fk_up_sub_user_id_issues_archive_assign_to_user_id
        foreign key (assign_to_user_id)
            references user_profile (sub_user_id),

    constraint fk_up_sub_user_id_issues_archive_checker_id
        foreign key (checker_id)
            references user_profile (sub_user_id),

    constraint fk_issues_archive_container_idic_issue_idic
        foreign key (idic)
            references issues_container (idic),

    constraint fk_project_project_id_issues_archive_project_id
        foreign key (project_id)
            references project (project_id)
);


create table if not exists issue_comments
(
    idc       bigserial unique not null,
    idi       bigint           not null,
    comment   jsonb            not null,
    is_edited boolean          not null default 'false',
    author_id varchar          not null,

    constraint issue_comments_pkey primary key (idc),

    constraint fk_issue_idi_issue_comments_idi
        foreign key (idi)
            references issue (idi) on delete cascade,

    constraint fk_user_profile_sub_user_id_issue_comments_author_id
        foreign key (author_id)
            references user_profile (sub_user_id)
)