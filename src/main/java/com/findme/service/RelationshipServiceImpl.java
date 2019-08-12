package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.util.UtilString;
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
    public Relationship save(String userIdFrom, String userIdTo) throws InternalServerException {
        if (userIdTo.equals(userIdFrom)) {
            throw new BadRequestException("You can't send any requests to yourself.");
        }
        Relationship relationship = relationshipCreator(userIdFrom, userIdTo, "REQUESTED");
        relationship.setDateCreated(LocalDate.now());
        relationship.setDateLastUpdated(LocalDate.now());
        return relationshipDao.save(relationship);
    }

    @Override
    public Relationship findById(Long id) throws InternalServerException {
        return relationshipDao.findById(id);
    }

    @Override
    public Relationship update(String userIdFrom, String userIdTo, String status) throws InternalServerException {
        Relationship relationship = relationshipCreator(userIdFrom, userIdTo, status);
        Relationship foundRelationship = findByIds(relationship.getUserFromId(), relationship.getUserToId());
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

    private Relationship relationshipCreator(String userIdFrom, String userIdTo, String newStatus) {
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        FriendRelationshipStatus relationshipStatus = UtilString.findFriendRelationshipStatus(newStatus);

        List<Relationship> allRequestsList = findByUserIdAndStatesRelationship(userIdFromL, FriendRelationshipStatus.REQUESTED);
        int allRequests = 0;
        if (allRequestsList != null) {
            allRequests = allRequestsList.size();
        }

        List<Relationship> allFriendsUserFromList = findByUserIdAndStatesRelationship(userIdFromL, FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserFrom = 0;
        if (allFriendsUserFromList != null) {
            allFriendsUserFrom = allFriendsUserFromList.size();
        }

        List<Relationship> allFriendsUserToList = findByUserIdAndStatesRelationship(userIdToL, FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserTo = 0;
        if (allFriendsUserToList != null) {
            allFriendsUserTo = allFriendsUserToList.size();
        }

        Relationship relationship = findByIds(userIdFromL, userIdToL);
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setUserFromId(userIdFromL);
            relationship.setUserToId(userIdToL);
            if (!FriendRelationshipStatus.REQUESTED.equals(relationshipStatus)) {
             throw new BadRequestException("This relationship doesn't exist. You can sent request only.");
            }
        }
        relationship.setFriendsQuantityUserFrom(allFriendsUserFrom);
        relationship.setFriendsQuantityUserTo(allFriendsUserTo);
        relationship.setRequestQuantity(allRequests);
        relationship = validation.relationshipValidation(userIdFromL, userIdToL, relationship, relationshipStatus);
        return relationship;
    }

}
