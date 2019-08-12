package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class RelationshipDaoImpl extends GeneralDao<Relationship> implements RelationshipDao {
    private String findByUserFromIdHql = "select r from Relationship r where r.userFromId = :userFromId";
    private String findByUserToIdHql = "select r from Relationship r where r.userToId = :userToId";
    private String findByIdFromAndIdToHql = "select r from Relationship r where r.userToId = :userToId " +
            " r.userFromId = :userFromId";

    private String findByIdsHql = "select r from Relationship r where (r.userToId = :userToId and" +
            " r.userFromId = :userFromId) or (r.userToId = :userFromIdInv and r.userFromId = :userToIdInv)";

    private String findByUserIdAndStatesRelationshipHql = "select r from Relationship r " +
            "where (r.userToId = :userId or r.userFromId = :userId) " +
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
    public List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = new ArrayList<>(entityManager.createQuery(findByUserFromIdHql, Relationship.class)
                    .setParameter("userFromId", userFromId)
                    .getResultList());
        } catch (NoResultException ex) {
            relationshipList = null;
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findByUserFromId userFromId: "
                    + userFromId);
        }
        return relationshipList;
    }

    @Override
    public List<Relationship> findByUserToId(Long userToId) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = new ArrayList<>(entityManager.createQuery(findByUserToIdHql, Relationship.class)
                    .setParameter("userToId", userToId)
                    .getResultList());
        } catch (NoResultException ex) {
            relationshipList = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUserToId userToId: "
                    + userToId);
        }
        return relationshipList;
    }


    @Override
    public Relationship findByIds(Long userFromId, Long userToId) throws InternalServerException {
        Relationship relationship;
        try {
            relationship = entityManager.createQuery(findByIdsHql, Relationship.class)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .setParameter("userFromIdInv", userFromId)
                    .setParameter("userToIdInv", userToId)
                    .getSingleResult();
        }  catch (NoResultException ex) {
            relationship = null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByIds userFromId = "
                    + userFromId + " userToId " + userToId);
        }
        return relationship;
    }


    @Override
    public List<Relationship> findByUserIdAndStatesRelationship(Long userId, FriendRelationshipStatus status) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = entityManager.createQuery(findByUserIdAndStatesRelationshipHql, Relationship.class)
                    .setParameter("userId", userId)
                    .setParameter("friendRelationshipStatus", status)
                    .getResultList();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findByUserIdAndStatesRelationship userId = " + userId +
                    " and friendRelationshipStatus: " + status);
        }
        return relationshipList;
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
    public Relationship findByIdFromAndIdTo(Long userFromId, Long userToId) throws InternalServerException {
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
            throw new InternalServerException("Something went wrong with findByIdFromAndIdTo userFromId = "
                    + userFromId + " userToId " + userToId);
        }
        return relationship;
    }
}
