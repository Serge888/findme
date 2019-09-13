package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class UserDaoImpl extends GeneralDao<User> implements UserDao {
    private String findByPhoneHql = "select u from User u where u.phone = :phoneNumber";
    private String findByEmailAddressHql = "select u from User u where u.emailAddress = :emailAddress";
    private String findTaggedUsersHql = "select u from User u left join Relationship r"
            + " on (r.userFrom.id = u.id) or (r.userTo.id = u.id)"
            + " where r.friendRelationshipStatus =  :friendRelationshipStatus"
            + " and u.id in (:usersTaggedIdList)"
            + " and (r.userFrom.id = :userPostedId or r.userTo.id = :userPostedId)";


    @Override
    public User findById(Long id) throws InternalServerException {
        User user;
        try {
            user =  entityManager.find(User.class, id);
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findById userId = " + id);
        }
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws InternalServerException {
        User user;
        try {
            user =  entityManager.createQuery(findByPhoneHql, User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        }  catch (NoResultException ex) {
            user = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByPhoneNumber Phone Number: "
                    + phoneNumber);
        }
        return user;
    }





    @Override
    public User findByEmailAddress(String emailAddress) throws InternalServerException {
        User user;
        try {
            user = entityManager.createQuery(findByEmailAddressHql, User.class)
                    .setParameter("emailAddress", emailAddress)

                    .getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByEmailAddress Email Address: "
                    + emailAddress);
        }
        return user;
    }

    @Override
    public List<User> findTaggedUsers(Long userPostedId, List<Long> usersTaggedIdList) throws InternalServerException {
        try {
            return entityManager.createQuery(findTaggedUsersHql, User.class)
                    .setParameter("friendRelationshipStatus", FriendRelationshipStatus.ACCEPTED)
                    .setParameter("usersTaggedIdList", usersTaggedIdList)
                    .setParameter("userPostedId", userPostedId)
                    .getResultList();

        } catch (NoResultException ex) {
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            e.printStackTrace();
            throw new InternalServerException("Something wrong with findTaggedUsers method.");
        }
    }
}
