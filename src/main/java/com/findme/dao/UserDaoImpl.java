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
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> rootUser = criteriaQuery.from(User.class);
            Root<Relationship> rootRelationship = criteriaQuery.from(Relationship.class);
            Predicate predicate = builder.conjunction();
            Expression<String> expression = rootUser.get("id");

            predicate = builder.and(predicate, builder.or(
                    builder.equal(rootUser.get("id"), rootRelationship.get("userFrom").get("id")),
                    builder.equal(rootUser.get("id"), rootRelationship.get("userTo").get("id"))));
            predicate = builder.and(predicate, builder.or(
                    builder.equal(rootRelationship.get("userFrom").get("id"), userPostedId),
                    builder.equal(rootRelationship.get("userTo").get("id"), userPostedId)));

            predicate = builder.and(predicate, builder.equal(rootRelationship.get("friendRelationshipStatus"),
                    FriendRelationshipStatus.ACCEPTED));

            predicate = builder.and(predicate, expression.in(usersTaggedIdList));

            criteriaQuery.select(rootUser).where(predicate);

            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("Something wrong with findTaggedUsers method.");
        }
    }
}
