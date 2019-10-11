package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.util.UtilString;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class PostDaoImpl extends GeneralDao<Post> implements PostDao {
    @Value("${maxPostsAsNews:20}")
    private Integer maxPostsAsNews;

    private String getAllPastsHql = "select p from Post p left join Relationship r " +
            "on (r.userFrom.id = p.userPosted.id and r.userTo.id = :userId) " +
            "or (r.userTo.id = p.userPosted.id and r.userFrom.id = :userId) " +
            "where r.friendRelationshipStatus = :relationshipStatus " +
            "or p.userPosted.id = :userId " +
            "order by p.datePosted desc";

    private String getByPostedUserHql = "select p from Post p " +
            "where p.userPosted.id = :userId " +
            "order by p.datePosted desc";

    private String getByPostedUserPageHql = "select p from Post p " +
            "where p.userPagePosted.id = :userId " +
            "and p.userPosted.id = :userId " +
            "order by p.datePosted desc";

    private String getFriendsPostsHql = "select p from Post p left join Relationship r " +
            "on (r.userFrom.id = p.userPosted.id and r.userTo.id = :userId) " +
            "or (r.userTo.id = p.userPosted.id and r.userFrom.id = :userId) " +
            "where r.friendRelationshipStatus = :relationshipStatus " +
            "order by p.datePosted desc";

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

    @Override
    public List<Post> findPostsByUserId(@NonNull PostFilter postFilter)
            throws BadRequestException, InternalServerException {
        List<Post> postList = new ArrayList<>();
        try {
            // показывать посты друзей
            if (postFilter.getFriends() != null && !"".equals(postFilter.getFriends())) {
                postList.addAll(entityManager.createQuery(getFriendsPostsHql, Post.class)
                        .setParameter("userId", postFilter.getLoggedInUser())
                        .setParameter("relationshipStatus", FriendRelationshipStatus.ACCEPTED)
                        .getResultList());
            }

            // показывать посты только того юзера, чья страница
            if (postFilter.getUserPageOwnerId() != null && !"".equals(postFilter.getUserPageOwnerId())) {
                postList.addAll(entityManager.createQuery(getByPostedUserPageHql, Post.class)
                        .setParameter("userId", UtilString.stringToLong(postFilter.getUserPageOwnerId()))
                        .getResultList());
            }

            // показывать посты определенного пользователя
            if (postFilter.getUserPostId() != null && !"".equals(postFilter.getUserPostId())) {
                postList.addAll(entityManager.createQuery(getByPostedUserHql, Post.class)
                        .setParameter("userId", UtilString.stringToLong(postFilter.getUserPostId()))
                        .getResultList());
            }

            // показывать все посты (по умолчанию). отсортированные по дате публикации (самый новый сверху)
            if ((postFilter.getFriends() == null || "".equals(postFilter.getFriends()))
                    && (postFilter.getUserPageOwnerId() == null || "".equals(postFilter.getUserPageOwnerId()))
                    && (postFilter.getUserPostId() == null || "".equals(postFilter.getUserPostId()))) {
                postList = entityManager.createQuery(getAllPastsHql, Post.class)
                        .setParameter("userId", postFilter.getLoggedInUser())
                        .setParameter("relationshipStatus", FriendRelationshipStatus.ACCEPTED)
                        .getResultList();
            }
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findPostsByUserId userId = "
                    + postFilter.getLoggedInUser());
        }
        return postList;
    }


    // for news limit 20 pcs
    @Override
    public List<Post> getPostsAsNews(PostFilter postFilter) {
        List<Post> postList;
        try {
            postList = new ArrayList<>(entityManager.createQuery(getFriendsPostsHql, Post.class)
                    .setParameter("userId", postFilter.getLoggedInUser())
                    .setParameter("relationshipStatus", FriendRelationshipStatus.ACCEPTED)
                    .setFirstResult(postFilter.getNewsIndexFrom())
                    .setMaxResults(maxPostsAsNews)
                    .getResultList());
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with getPostsAsNews userId = "
                    + postFilter.getLoggedInUser());
        }

        return postList;
    }
}