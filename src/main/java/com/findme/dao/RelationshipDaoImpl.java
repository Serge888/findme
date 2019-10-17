package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.util.UtilString;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class RelationshipDaoImpl extends GeneralDao<Relationship> implements RelationshipDao {

    private String findByUserFromIdHql = "select r from Relationship r where r.userFrom = :userFromId";
    private String findByUserToIdHql = "select r from Relationship r where r.userTo = :userToId";
    private String findByIdFromAndIdToHql = "select r from Relationship r where r.userTo.id = :userToId " +
            "and r.userFrom.id = :userFromId";
    private String findByIdsHql = "select r from Relationship r where (r.userTo = :userToId and" +
            " r.userFrom = :userFromId) or (r.userTo = :userFromIdInv and r.userFrom = :userToIdInv)";
    private String findByUserIdAndStatesRelationshipHql = "select r from Relationship r " +
            "where (r.userTo = :userIdTo or r.userFrom = :userIdFrom) " +
            "and r.friendRelationshipStatus = :friendRelationshipStatus";
    private String userFriendsQuantityHqlHql = "select count(r) from Relationship r " +
            "where (r.userTo = :userIdTo or r.userFrom = :userIdFrom) " +
            "and r.friendRelationshipStatus = :friendRelationshipStatus";



    @Override
    public Relationship findById(Long id) throws InternalServerException {
        Relationship relationship;
        try {
            relationship = entityManager.find(Relationship.class, id);
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findById userId = " + id);
        }
        return relationship;
    }

    @Override
    public List<Relationship> findByUserFrom(User userFrom) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = new ArrayList<>(entityManager.createQuery(findByUserFromIdHql, Relationship.class)
                    .setParameter("userFromId", userFrom)
                    .getResultList());
        } catch (NoResultException ex) {
            relationshipList = null;
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findByUserFrom userFromId: "
                    + userFrom);
        }
        return relationshipList;
    }

    @Override
    public List<Relationship> findByUserTo(User userTo) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = new ArrayList<>(entityManager.createQuery(findByUserToIdHql, Relationship.class)
                    .setParameter("userToId", userTo)
                    .getResultList());
        } catch (NoResultException ex) {
            relationshipList = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUserTo userToId: "
                    + userTo.getId());
        }
        return relationshipList;
    }


    @Override
    public Relationship findByUsers(User userFrom, User userTo) throws InternalServerException{
        Relationship relationship;
        try {
            relationship = entityManager.createQuery(findByIdsHql, Relationship.class)
                    .setParameter("userFromId", userFrom)
                    .setParameter("userToId", userTo)
                    .setParameter("userFromIdInv", userFrom)
                    .setParameter("userToIdInv", userTo)
                    .getSingleResult();
        }  catch (NoResultException ex) {
            relationship = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUsers userFromId = "
                    + userFrom.getId() + " userToId " + userTo.getId());
        }
        return relationship;
    }


    @Override
    public List<Relationship> findByUserAndStatesRelationship(User user, FriendRelationshipStatus status) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = entityManager.createQuery(findByUserIdAndStatesRelationshipHql, Relationship.class)
                    .setParameter("userIdFrom", user)
                    .setParameter("userIdTo", user)
                    .setParameter("friendRelationshipStatus", status)
                    .getResultList();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUserAndStatesRelationship userId = " + user.getId() +
                    " and friendRelationshipStatus: " + status);
        }
        return relationshipList;
    }



    @Override
    public Integer relationshipQuantityByUser(User user, FriendRelationshipStatus status) throws InternalServerException {
       Integer countFriends;
        try {
            String string = entityManager.createQuery(userFriendsQuantityHqlHql)
                    .setParameter("userIdFrom", user)
                    .setParameter("userIdTo", user)
                    .setParameter("friendRelationshipStatus", status)
                    .getSingleResult().toString();
            countFriends = UtilString.stringToInteger(string);
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with relationshipQuantityByUser userId = " + user.getId());
        }
        return countFriends;
    }


    @Override
    public Relationship delete(Relationship relationship) throws InternalServerException {
        try {
            entityManager.remove(findById(relationship.getId()));
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong with delete entity: " + relationship);
        }
        return relationship;
    }


    @Override
    public Relationship findByUserFromAndUserTo(Long userFromId, Long userToId) throws InternalServerException {
        Relationship relationship;
        try {
            relationship = entityManager.createQuery(findByIdFromAndIdToHql, Relationship.class)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.getMessage();
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUserFromAndUserTo userFromId = "
                    + userFromId + " userToId " + userToId);
        }
        return relationship;
    }
}
