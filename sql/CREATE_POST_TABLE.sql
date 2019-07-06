CREATE TABLE post (
    id numeric,
    CONSTRAINT post_pk PRIMARY KEY (id),
    message varchar(2000),
    date_post TIMESTAMP WITH TIME ZONE,
    user_posted_id numeric,
    CONSTRAINT user_posted_id_fk FOREIGN KEY (user_posted_id) REFERENCES users (id)
);


