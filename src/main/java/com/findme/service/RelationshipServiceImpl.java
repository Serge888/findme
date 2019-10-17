package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.findme.models.User;
import com.findme.relationshipValidation.RelationshipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
                                   @Lazy RelationshipValidation validation, UserService userService) {
        this.relationshipDao = relationshipDao;
        this.validation = validation;
        this.userService = userService;
    }

    @Override
    public Relationship save(Long userIdFrom, Long userIdTo) throws InternalServerException {
        if (userIdTo.equals(userIdFrom)) {
            throw new BadRequestException("You can't send any requests to yourself.");
        }
        Relationship relationship = relationshipCreatorForSave(userIdFrom, userIdTo);
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
        Relationship relationship = relationshipCreatorForUpdate(userIdFrom, userIdTo, status);
        Relationship foundRelationship = findByIds(relationship.getUserFrom().getId(),
                relationship.getUserTo().getId());
        if (foundRelationship != null) {
            foundRelationship.setFriendRelationshipStatus(relationship.getFriendRelationshipStatus());
            foundRelationship.setUserFrom(relationship.getUserFrom());
            foundRelationship.setUserTo(relationship.getUserTo());
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
        return relationshipDao.findByUserFromAndUserTo(userFromId, userToId);
    }


    @Override
    public Relationship findByIds(Long userFromId, Long userToId) throws InternalServerException {
        if (userFromId == null || userToId == null) {
            throw new BadRequestException("No one user id cannot be null");
        }
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


    private Relationship relationshipCreatorForUpdate(Long userIdFrom, Long userIdTo, FriendRelationshipStatus newStatus) {
        User userFrom = userService.findById(userIdFrom);
        User userTo = userService.findById(userIdTo);
        TechRelationshipData techRelationshipData = new TechRelationshipData(userFrom, userTo, newStatus);
        Relationship relationship = findByIds(userIdFrom, userIdTo);
        if (relationship == null) {
            throw new BadRequestException("Invalid request");
        }
        relationship = validation.relationshipValidation(relationship, techRelationshipData);
        return relationship;
    }

    private Relationship relationshipCreatorForSave(Long userIdFrom, Long userIdTo) {
        User userFrom = userService.findById(userIdFrom);
        User userTo = userService.findById(userIdTo);
        TechRelationshipData techRelationshipData = new TechRelationshipData(userFrom, userTo,
                FriendRelationshipStatus.REQUESTED);
        Relationship relationship = findByIds(userIdFrom, userIdTo);
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setUserFrom(userFrom);
            relationship.setUserTo(userTo);
        } else {
            throw new BadRequestException("Invalid request");
        }
        relationship = validation.relationshipValidation(relationship, techRelationshipData);
        return relationship;
    }

}
