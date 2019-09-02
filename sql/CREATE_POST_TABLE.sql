CREATE TABLE post (
    id numeric,
    CONSTRAINT post_pk PRIMARY KEY (id),
    message varchar(2000),
    date_post TIMESTAMP WITH TIME ZONE,
    user_posted_id numeric,
    CONSTRAINT user_posted_id_fk FOREIGN KEY (user_posted_id) REFERENCES users (id)
);

ALTER TABLE post ALTER COLUMN message TYPE VARCHAR(200) USING message::VARCHAR(200);

ALTER TABLE post ADD user_page_posted_id NUMERIC;
ALTER TABLE post ADD CONSTRAINT user_page_posted_id_fk FOREIGN KEY (user_page_posted_id) REFERENCES users (id);

ALTER TABLE post ADD location VARCHAR;




