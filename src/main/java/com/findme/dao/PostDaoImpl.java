package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class PostDaoImpl extends GeneralDao<Post> implements PostDao {


    @Override
    public Post findById(Long id) throws InternalServerException, NotFoundException {
        Post post;
        try {
            post = entityManager.find(Post.class, id);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findById postId = " + id);
        }
        if (post == null) {
            throw new NotFoundException("Post id " + id + " was not found");
        }
        return post;
    }
}
