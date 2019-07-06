package com.findme.dao;

import com.findme.models.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao {

    Post save(Post post);
    Post update(Post post);
    Post delete(Long id);
    Post findById(Long id);

}
