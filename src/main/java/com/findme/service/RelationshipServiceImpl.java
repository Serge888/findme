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
    public Relationship save(Long userIdFrom, Long userIdTo) throws InternalServerException {
        if (userIdTo.equals(userIdFrom)) {
            throw new BadRequestException("You can't send any requests to yourself.");
        }
        Relationship relationship = relationshipCreator(userIdFrom, userIdTo, FriendRelationshipStatus.REQUESTED);
        relationship.setDateCreated(LocalDate.now());
        relationship.setDateLastUpdated(LocalDate.now());
        return relationshipDao.save(relationship);
    }

    @Override
    public Relationship findById(Long id) throws InternalServerException {
        return relationshipDao.findById(id);
    }

    @Override
    public Relationship update(Long userIdFrom, Long userIdTo, FriendRelationshipStatus status) throws InternalServerException {
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


    private Relationship relationshipCreator(Long userIdFrom, Long userIdTo, FriendRelationshipStatus newStatus) {
        int allRequests = relationshipQuantityByUserId(userIdFrom, FriendRelationshipStatus.REQUESTED);
        int allFriendsUserFrom = relationshipQuantityByUserId(userIdFrom, FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserTo = relationshipQuantityByUserId(userIdTo, FriendRelationshipStatus.ACCEPTED);
        User userFrom = userService.findById(userIdFrom);
        User userTo = userService.findById(userIdTo);
        Relationship relationship = findByIds(userIdFrom, userIdTo);
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setUserFrom(userFrom);
            relationship.setUserTo(userTo);
            if (!FriendRelationshipStatus.REQUESTED.equals(newStatus)) {
             throw new BadRequestException("This relationship doesn't exist. You can sent request only.");
            }
        }
        relationship = validation.relationshipValidation(userFrom, userTo, relationship,
                newStatus, allRequests, allFriendsUserFrom, allFriendsUserTo);
        return relationship;
    }

}
