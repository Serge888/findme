CREATE TABLE role (
    id numeric,
    CONSTRAINT role_pk PRIMARY KEY (id),
    role_status varchar(2000)
);


CREATE TABLE users_role (
    user_id NUMERIC NOT NULL,
    role_id NUMERIC NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_id_fk FOREIGN KEY(user_id) REFERENCES users (id),
    CONSTRAINT role_id_fk FOREIGN KEY(role_id) REFERENCES role (id)
);