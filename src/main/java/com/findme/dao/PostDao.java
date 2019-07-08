package com.findme.dao;

import com.findme.models.Post;

public interface PostDao {

    Post save(Post post);
    Post update(Post post);
    Post delete(Post post);
    Post findById(Long id);

}
