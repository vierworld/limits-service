CREATE SCHEMA IF NOT EXISTS CSEP;

create table csep.cl_limit
(
	client_id bigint not null,
	consumed numeric(32, 2) not null,
	op_stamp timestamptz not null,
	daily_limit numeric(32, 2) not null,
	primary key (client_id)
);

comment on table csep.cl_limit is 'Таблица с учетом лимитов на клиента';
comment on column csep.cl_limit.client_id is 'Id клиента';
comment on column csep.cl_limit.consumed is 'Потреблено за текущий день';
comment on column csep.cl_limit.op_stamp is 'Дата последнего потребления лимита';
comment on column csep.cl_limit.daily_limit is 'Дневной лимит клиента';



create table csep.cl_operation
(
	id bigserial not null,
	client_id bigint not null,
	consumed numeric(32, 2) not null,
	op_stamp timestamptz not null,
	rmv boolean not null,
	primary key (id)
);

create index idx_cl_operation_client_id_date on csep.cl_operation(client_id, op_stamp);



comment on table csep.cl_operation is 'Таблица с историей операций';
comment on column csep.cl_operation.id is 'Id операции';
comment on column csep.cl_operation.client_id is 'Id клиента';
comment on column csep.cl_operation.consumed is 'Потреблено за операцию';
comment on column csep.cl_operation.op_stamp is 'Дата операции';
comment on column csep.cl_operation.rmv is 'Факт отмены операции';


insert into csep.cl_limit(client_id, consumed, op_stamp, daily_limit)
select generate_series(1,100) as client_id, 0 as consumed,
	clock_timestamp() as op_stamp, 10000 as daily_limit;