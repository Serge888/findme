CREATE TABLE relationship (
    id numeric,
    CONSTRAINT relationship_pk PRIMARY KEY (id),
    id_user_from numeric,
    CONSTRAINT id_user_from_fk FOREIGN KEY (id_user_from) REFERENCES users (id),
    id_user_to numeric,
    CONSTRAINT id_user_to_fk FOREIGN KEY (id_user_to) REFERENCES users (id),
    id_relation_ship_status numeric
);
