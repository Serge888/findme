package com.findme.service;


import com.findme.dao.PostDao;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Post;
import com.findme.models.TechPostData;
import com.findme.models.User;
import com.findme.util.UtilString;
import com.findme.validation.postValidation.PostValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao postDao;
    private PostValidation postValidation;
    private UserService userService;

    @Autowired
    public PostServiceImpl(PostDao postDao, PostValidation postValidation, UserService userService) {
        this.postDao = postDao;
        this.postValidation = postValidation;
        this.userService = userService;
    }

    @Override
    public Post save(TechPostData techPostData) throws InternalServerException {
        Post post = postCreator(techPostData);
        postValidation.postValidation(post);
        return postDao.save(post);
    }


    @Override
    public Post delete(Post post) throws InternalServerException {
        return postDao.delete(post);
    }

    @Override
    public Post findById(Long id) throws NotFoundException, InternalServerException {
        return postDao.findById(id);
    }

    private Post postCreator(TechPostData techPostData) {
        Long userPostedIdL = UtilString.stringToLong(techPostData.getUserPostedId());
        Long userPagePostedIdL = UtilString.stringToLong(techPostData.getUserPagePostedId());

        Post post = new Post();
        post.setUserPosted(userService.findById(userPostedIdL));
        post.setUserPagePosted(userService.findById(userPagePostedIdL));
        post.setMessage(techPostData.getMessage());
        post.setLocation(techPostData.getLocation());

        List<Long> ids = UtilString.stringToLongList(techPostData.getUsersTagged());
        List<User> taggedUserList = new ArrayList<>();
        for (Long id : ids) {
            User user = userService.findById(id);
            if (user != null) {
                taggedUserList.add(user);
            }
        }
        post.setUsersTagged(taggedUserList);
        return post;
    }

}
