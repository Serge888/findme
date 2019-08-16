package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.util.UtilString;
import com.findme.validation.RelationshipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    private RelationshipDao relationshipDao;
    private RelationshipValidation validation;
    private UserService userService;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao,
                                   RelationshipValidation validation, UserService userService) {
        this.relationshipDao = relationshipDao;
        this.validation = validation;
        this.userService = userService;
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
        Relationship foundRelationship = findByIds(relationship.getUserFrom().getId(),
                relationship.getUserTo().getId());
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
        User userFrom = userService.findById(userFromId);
        User userTo = userService.findById(userToId);
        return relationshipDao.findByUserFromAndUserTo(userFrom, userTo);
    }


    @Override
    public Relationship findByIds(Long userFromId, Long userToId) throws InternalServerException {
        User userFrom = userService.findById(userFromId);
        User userTo = userService.findById(userToId);
        return relationshipDao.findByUsers(userFrom, userTo);
    }

    @Override
    public List<Relationship> findByUserIdAndStatesRelationship(Long userId, FriendRelationshipStatus status) throws InternalServerException {
        User user = userService.findById(userId);
        return relationshipDao.findByUserAndStatesRelationship(user, status);
    }

    @Override
    public Integer relationshipQuantityByUserId(Long userId, FriendRelationshipStatus status) {
        User user = userService.findById(userId);
        return relationshipDao.relationshipQuantityByUser(user, status);
    }


    @Override
    public List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException {
        User user = userService.findById(userFromId);
        return relationshipDao.findByUserFrom(user);
    }

    @Override
    public List<Relationship> findByUserToId(Long userToId) throws InternalServerException {
        User user = userService.findById(userToId);
        return relationshipDao.findByUserTo(user);
    }


    private Relationship relationshipCreator(String userIdFrom, String userIdTo, String newStatus) {
        Long userIdFromL = UtilString.stringToLong(userIdFrom);
        Long userIdToL = UtilString.stringToLong(userIdTo);
        FriendRelationshipStatus relationshipStatus = UtilString.findFriendRelationshipStatus(newStatus);

        List<Relationship> allRequestsList = findByUserIdAndStatesRelationship(userIdFromL, FriendRelationshipStatus.REQUESTED);
        int allRequests = 0;
        if (allRequestsList != null) {
            allRequests = relationshipQuantityByUserId(userIdFromL, FriendRelationshipStatus.REQUESTED);
        }

        List<Relationship> allFriendsUserFromList = findByUserIdAndStatesRelationship(userIdFromL, FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserFrom = 0;
        if (allFriendsUserFromList != null) {
            allFriendsUserFrom = relationshipQuantityByUserId(userIdFromL, FriendRelationshipStatus.ACCEPTED);
        }

        List<Relationship> allFriendsUserToList = findByUserIdAndStatesRelationship(userIdToL, FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserTo = 0;
        if (allFriendsUserToList != null) {
            allFriendsUserTo = relationshipQuantityByUserId(userIdToL, FriendRelationshipStatus.ACCEPTED);
        }

        User userFrom = userService.findById(userIdFromL);
        User userTo = userService.findById(userIdToL);
        Relationship relationship = findByIds(userIdFromL, userIdToL);
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setUserFrom(userFrom);
            relationship.setUserTo(userTo);
            if (!FriendRelationshipStatus.REQUESTED.equals(relationshipStatus)) {
             throw new BadRequestException("This relationship doesn't exist. You can sent request only.");
            }
        }
        relationship = validation.relationshipValidation(userFrom, userTo, relationship,
                relationshipStatus, allRequests, allFriendsUserFrom, allFriendsUserTo);
        return relationship;
    }

}
