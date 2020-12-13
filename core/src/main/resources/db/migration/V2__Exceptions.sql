create table atlas_exceptions (
    aeid int unique not null
        constraint aeid_pkey primary key ,
    key varchar not null default 'ATLAS',
    section varchar not null,
    messageInLog text not null,
    messageInThrow text not null,
    title text not null,
    description text
)