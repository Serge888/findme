package com.findme.dao;

import com.findme.exception.InternalServerException;
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

    @Override
    public Relationship findById(Long id) throws InternalServerException {
        Relationship relationship;
        try {
            relationship =  entityManager.find(Relationship.class, id);
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new InternalServerException("Something went wrong with findById userId = " + id);
        }
        return relationship;
    }

    @Override
    public List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException {
        List<Relationship> relationshipList;
        try {
            relationshipList = new ArrayList<>(entityManager.createNamedQuery("Relationship.findByUserFromId", Relationship.class)
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
            relationshipList = new ArrayList<>(entityManager.createNamedQuery("Relationship.findByUserToId", Relationship.class)
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
    public Relationship delete(Relationship relationship) throws InternalServerException {
        try {
            entityManager.remove(findById(relationship.getId()));
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong with delete entity: " + relationship);
        }
        return relationship;
    }
}
