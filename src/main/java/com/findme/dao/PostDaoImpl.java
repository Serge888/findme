package com.findme.dao;

import com.findme.models.Post;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class PostDaoImpl extends GeneralDao<Post> implements PostDao {


    @Override
    public Post findById(Long id) {
        return entityManager.find(Post.class, id);
    }
}
