CREATE TABLE users (
    id numeric,
    CONSTRAINT user_pk PRIMARY KEY (id),
    first_name varchar(128),
    last_name varchar(128),
    phone varchar(28),
    country varchar(128),
    city varchar(128),
    age numeric(3,0),
    date_registered TIMESTAMP WITH TIME ZONE,
    date_last_active TIMESTAMP WITH TIME ZONE,
    religion varchar(128),
    school varchar(128),
    university varchar(128),
    relation_ship_status varchar(128)
);


alter table users add email_address varchar(128);
alter table users add password varchar(128);