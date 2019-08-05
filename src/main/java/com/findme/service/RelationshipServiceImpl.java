package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    private RelationshipDao relationshipDao;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao) {
        this.relationshipDao = relationshipDao;
    }

    @Override
    public Relationship save(Relationship relationship) throws InternalServerException {
        if (relationship.getUserToId().equals(relationship.getUserFromId())) {
            throw new BadRequestException("You can't send any requests to yourself.");
        }
        List<Relationship> relationshipList =
                relationshipDao.findByUserFromId(relationship.getUserFromId());
        if (relationshipList != null && relationshipList.size() > 0) {
            for (Relationship existingRelationship : relationshipList) {
                if (existingRelationship.getUserToId().equals(relationship.getUserToId())) {
                    throw new BadRequestException("You've already sent request. The status of request is "
                    + existingRelationship.getFriendRelationshipStatus());
                }
            }
        }
        relationship.setFriendRelationshipStatus(FriendRelationshipStatus.REQUEST_SENT);
        return relationshipDao.save(relationship);
    }

    @Override
    public Relationship findById(Long id) throws InternalServerException {
        return relationshipDao.findById(id);
    }

    @Override
    public Relationship update(Relationship relationship) throws InternalServerException {
        List<Relationship> relationshipList =
                relationshipDao.findByUserFromId(relationship.getUserFromId());
        if (relationshipList != null && relationshipList.size() > 0) {
            for (Relationship existingRelationship : relationshipList) {
                if (existingRelationship.getUserToId().equals(relationship.getUserToId())) {
                    Relationship foundRelationship = relationshipDao.findById(existingRelationship.getId());
                    foundRelationship.setFriendRelationshipStatus(relationship.getFriendRelationshipStatus());
                    return relationshipDao.update(foundRelationship);
                }
            }
        }
        throw new NotFoundException("Friend relationship doesn't exist.");
    }

    @Override
    public Relationship delete(Relationship relationship) throws InternalServerException {
        return relationshipDao.delete(relationship);
    }

    @Override
    public Relationship cancelRelationship(Long userIdFrom, Long userIdTo) throws InternalServerException {
        List<Relationship> relationshipList = relationshipDao.findByUserFromId(userIdFrom);
        if (relationshipList != null && relationshipList.size() > 0) {
            for (Relationship relationship : relationshipList) {
                if (relationship.getUserToId().equals(userIdTo)
                        && FriendRelationshipStatus.REQUEST_SENT.equals(relationship.getFriendRelationshipStatus())) {
                    return relationshipDao.delete(relationship);
                }
            }
        }
        throw new BadRequestException("You can't cancel your request");
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
