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
        Post post = postCreator(techPostData);
        postLengthValidation(post);
        postPageValidation(post);
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

    @Override
    public List<Post> findPostsByUserId(PostFilter postFilter) throws BadRequestException, InternalServerException {
        return postDao.findPostsByUserId(postFilter);
    }

    @Override
    public List<Post> getPostsAsNews(PostFilter postFilter) throws BadRequestException, InternalServerException {
        return postDao.getPostsAsNews(postFilter);
    }

    @Override
    public void postLengthValidation(@NonNull Post post) throws BadRequestException {
        if (post.getMessage() != null && post.getMessage().length() <= 200) {
            return;
        }
        throw new BadRequestException("Post can't be null or more than 200 characters");
    }


    @Override
    public void postPageValidation(@NonNull Post post) throws BadRequestException {
        if (post.getUserPagePosted() == null) {
            throw new BadRequestException("UserPagePosted can't be null.");
        }
        if (post.getUserPosted().equals(post.getUserPagePosted())) {
            return;
        }
        List<Relationship> relationshipList =
                relationshipService.findByUserIdAndStatesRelationship(post.getUserPosted().getId(),
                FriendRelationshipStatus.ACCEPTED);

        if (relationshipList != null && relationshipList.size() > 0) {
            for (Relationship relationship : relationshipList) {
                if (post.getUserPagePosted().equals(relationship.getUserFrom())
                        || post.getUserPagePosted().equals(relationship.getUserTo())) {
                    return;
                }
            }
        }

        throw new BadRequestException("You can create post on your or your friends' pages.");
    }

    private Post postCreator(TechPostData techPostData) {
        Long userPagePostedIdL = UtilString.stringToLong(techPostData.getUserPagePostedId());
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
