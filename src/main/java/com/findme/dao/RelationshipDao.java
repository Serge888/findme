package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;

import java.util.List;

public interface RelationshipDao {

    Relationship save(Relationship relationship) throws InternalServerException;
    Relationship findById(Long id) throws InternalServerException;
    Relationship update(Relationship relationship) throws InternalServerException;
    Relationship delete(Relationship relationship) throws InternalServerException;
    Relationship findByUserFromAndUserTo(Long userFromId, Long userToId) throws InternalServerException;
    List<Relationship> findByUserFrom(User userFrom) throws InternalServerException;
    List<Relationship> findByUserTo(User userTo) throws InternalServerException;
    Relationship findByUsers(User userFrom, User userTo) throws InternalServerException;
    List<Relationship> findByUserAndStatesRelationship(User user, FriendRelationshipStatus status) throws InternalServerException;
    Integer relationshipQuantityByUser(User user, FriendRelationshipStatus status) throws InternalServerException;


}
