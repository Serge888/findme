CREATE TABLE relationship (
    id numeric,
    CONSTRAINT relationship_pk PRIMARY KEY (id),
    id_user_from numeric,
    CONSTRAINT id_user_from_fk FOREIGN KEY (id_user_from) REFERENCES users (id),
    id_user_to numeric,
    CONSTRAINT id_user_to_fk FOREIGN KEY (id_user_to) REFERENCES users (id),
    id_relation_ship_status numeric
);


alter table relationship
    add date_created TIMESTAMP WITH TIME ZONE;

alter table relationship
    add date_last_updated TIMESTAMP WITH TIME ZONE;