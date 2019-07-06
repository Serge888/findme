package com.findme.service;


import com.findme.models.Post;
import com.findme.dao.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Override
    public Post save(Post post) {
        return postDao.save(post);
    }

    @Override
    public Post update(Post post) {
        post.setDatePosted(new Date());
        return postDao.update(post);
    }

    @Override
    public Post delete(Long id) {
        return postDao.delete(id);
    }

    @Override
    public Post findById(Long id) {
        return postDao.findById(id);
    }
}
