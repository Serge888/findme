package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    private RelationshipDao relationshipDao;
    private Validation validation;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao, Validation validation) {
        this.relationshipDao = relationshipDao;
        this.validation = validation;
    }

    @Override
    public Relationship save(Relationship relationship) throws InternalServerException {
        if (relationship.getUserToId().equals(relationship.getUserFromId())) {
            throw new BadRequestException("You can't send any requests to yourself.");
        }
        validation.requestQuantityValidation(relationship.getUserFromId());
        validation.friendQuantityValidation(relationship.getUserFromId());
        validation.friendQuantityValidation(relationship.getUserToId());
        Relationship foundRelationship =
                relationshipDao.findByIdFromAndIdTo(relationship.getUserFromId(), relationship.getUserToId());
        if (foundRelationship != null) {
            throw new BadRequestException("You've already sent request. The status of request is "
                    + foundRelationship.getFriendRelationshipStatus());
        }
        relationship.setFriendRelationshipStatus(FriendRelationshipStatus.REQUESTED);
        relationship.setDateCreated(LocalDate.now());
        relationship.setDateLastUpdated(LocalDate.now());
        return relationshipDao.save(relationship);
    }

    @Override
    public Relationship findById(Long id) throws InternalServerException {
        return relationshipDao.findById(id);
    }

    @Override
    public Relationship update(Relationship relationship) throws InternalServerException {
        if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
            validation.friendQuantityValidation(relationship.getUserFromId());
            validation.friendQuantityValidation(relationship.getUserToId());
        }
        if (FriendRelationshipStatus.DELETED.equals(relationship.getFriendRelationshipStatus())) {
            validation.deleteRelationshipValidation(relationship.getUserFromId(), relationship.getUserToId());
        }
        Relationship foundRelationship =
                relationshipDao.findByIdFromAndIdTo(relationship.getUserFromId(), relationship.getUserToId());
        if (foundRelationship != null) {
            foundRelationship.setFriendRelationshipStatus(relationship.getFriendRelationshipStatus());
            foundRelationship.setDateLastUpdated(LocalDate.now());
            return relationshipDao.update(foundRelationship);
        }
        throw new NotFoundException("Friend relationship doesn't exist.");
    }

    @Override
    public Relationship delete(Relationship relationship) throws InternalServerException {
        return relationshipDao.delete(relationship);
    }

    @Override
    public Relationship findByIdFromAndIdTo(Long userFromId, Long userToId) throws InternalServerException {
        return relationshipDao.findByIdFromAndIdTo(userFromId, userToId);
    }

    @Override
    public Relationship cancelRelationship(Long userIdFrom, Long userIdTo) throws InternalServerException {
        Relationship foundRelationship = relationshipDao.findByIdFromAndIdTo(userIdFrom, userIdTo);
        if (foundRelationship != null
                && FriendRelationshipStatus.REQUESTED.equals(foundRelationship.getFriendRelationshipStatus())) {
            return relationshipDao.delete(foundRelationship);
        }
        throw new BadRequestException("You can't cancel your request");
    }

    @Override
    public Relationship findByIds(Long userFromId, Long userToId) throws InternalServerException {
        return relationshipDao.findByIds(userFromId, userToId);
    }

    @Override
    public List<Relationship> findByUserIdAndStatesRelationship(Long userId, FriendRelationshipStatus status) throws InternalServerException {
        return relationshipDao.findByUserIdAndStatesRelationship(userId, status);
    }


    @Override
    public List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException {
        return relationshipDao.findByUserFromId(userFromId);
    }

    @Override
    public List<Relationship> findByUserToId(Long userToId) throws InternalServerException {
        return relationshipDao.findByUserToId(userToId);
    }


}
