alter table task alter column priority type varchar;

alter table task alter column priority set default 'Medium';

alter table task drop column status;
