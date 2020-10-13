/*create trigger call_table_generator_task_numbers
    after insert on project execute procedure table_generator_task_numbers();

create function table_generator_task_numbers() returns  trigger
as $tgtn$
        begin
            NEW.
        end;
    $tgtn$*/

create index not_unique_index_project_role on project_role_member (project_id, role_id);