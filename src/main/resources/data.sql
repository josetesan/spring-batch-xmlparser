create table laboratorio
(
    codigolaboratorio integer not null primary key ,
    laboratorio       varchar(128) not null ,
    direccion         varchar(128) not null ,
    codigopostal      varchar(12) ,
    localidad         varchar(64) ,
    cif               varchar(16)
);

create index laboratorio_cp_idx on laboratorio(codigopostal);
