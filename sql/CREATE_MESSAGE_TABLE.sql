CREATE TABLE message (
    id numeric,
    CONSTRAINT message_pk PRIMARY KEY (id),
    text varchar(2000),
    date_sent TIMESTAMP WITH TIME ZONE,
    date_read TIMESTAMP WITH TIME ZONE,
    user_from_id numeric,
    CONSTRAINT user_from_id_fk FOREIGN KEY (user_from_id) REFERENCES users (id),
    user_to_id numeric,
    CONSTRAINT user_to_id_fk FOREIGN KEY (user_to_id) REFERENCES users (id)
);


