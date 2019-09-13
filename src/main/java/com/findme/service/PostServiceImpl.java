package com.findme.service;


import com.findme.dao.PostDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.*;
import com.findme.util.UtilString;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao postDao;
    private UserService userService;
    private RelationshipService relationshipService;

    @Autowired
    public PostServiceImpl(PostDao postDao, UserService userService,
                           RelationshipService relationshipService) {
        this.postDao = postDao;
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @Override
    public Post save(TechPostData techPostData) throws InternalServerException {
        if (techPostData.getMessage() == null || techPostData.getMessage().length() > 200) {
            throw new BadRequestException("Post can't be null or more than 200 characters");
        }
        Long userPagePostedIdL = UtilString.stringToLong(techPostData.getUserPagePostedId());
        postPageValidation(techPostData, userPagePostedIdL);
        return postDao.save(postCreator(techPostData, userPagePostedIdL));
    }


    @Override
    public Post delete(Post post) throws InternalServerException {
        return postDao.delete(post);
    }

    @Override
    public Post findById(Long id) throws NotFoundException, InternalServerException {
        return postDao.findById(id);
    }

    @Override
    public List<Post> findPostsByUserId(PostFilter postFilter) throws BadRequestException, InternalServerException {
        return postDao.findPostsByUserId(postFilter);
    }

    @Override
    public List<Post> getPostsAsNews(PostFilter postFilter) throws BadRequestException, InternalServerException {
        return postDao.getPostsAsNews(postFilter);
    }


    private void postPageValidation(@NonNull TechPostData techPostData, Long userPagePostedIdL)
            throws BadRequestException {
        if (techPostData.getUserPagePostedId() == null) {
            throw new BadRequestException("UserPagePosted can't be null.");
        }
        if (techPostData.getUserPosted().getId().equals(userPagePostedIdL)) {
            return;
        }
        Relationship relationship = relationshipService.findByIds(techPostData.getUserPosted().getId(),
                userPagePostedIdL);

        if (relationship != null
                && FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
            return;
        }

        throw new BadRequestException("You can create post on your or your friends' pages.");
    }

    private Post postCreator(TechPostData techPostData, Long userPagePostedIdL) {
        Post post = new Post();
        post.setUserPosted(techPostData.getUserPosted());
        post.setUserPagePosted(userService.findById(userPagePostedIdL));
        post.setMessage(techPostData.getMessage());
        post.setLocation(techPostData.getLocation());
        post.setDatePosted(LocalDate.now());

        List<Long> ids = UtilString.stringToLongList(techPostData.getUsersTagged());
        List<User> taggedUserList = userService.findTaggedUsers(techPostData.getUserPosted().getId(), ids);
        post.setUsersTagged(taggedUserList);
        return post;
    }

}
