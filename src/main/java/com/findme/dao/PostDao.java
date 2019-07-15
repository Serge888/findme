package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;

public interface PostDao {

    Post save(Post post) throws InternalServerException;
    Post update(Post post) throws InternalServerException;
    Post delete(Post post) throws InternalServerException;
    Post findById(Long id) throws InternalServerException, NotFoundException;

}
