package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.PostFilter;

import java.util.List;

public interface PostDao {

    Post save(Post post) throws InternalServerException;
    Post update(Post post) throws InternalServerException;
    Post delete(Post post) throws InternalServerException;
    Post findById(Long id) throws InternalServerException, NotFoundException;
    List<Post> findPostsByUserId(PostFilter postFilter) throws BadRequestException, InternalServerException;
    List<Post> getPostsAsNews(PostFilter postFilter) throws BadRequestException, InternalServerException;

}
