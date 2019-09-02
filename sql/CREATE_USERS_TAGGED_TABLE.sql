CREATE TABLE users_tagged (
    user_id NUMERIC NOT NULL,
    post_id NUMERIC NOT NULL,
    PRIMARY KEY (user_id,post_id),
    CONSTRAINT user_id_fk FOREIGN KEY(user_id) REFERENCES users (id),
    CONSTRAINT post_id_fk FOREIGN KEY(post_id) REFERENCES post (id)
);