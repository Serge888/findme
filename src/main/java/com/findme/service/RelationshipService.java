package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;

import java.util.List;

public interface RelationshipService {

    Relationship save(String userIdFrom, String userIdTo) throws InternalServerException;
    Relationship findById(Long id) throws InternalServerException;
    Relationship update(String userIdFrom, String userIdTo, String status) throws InternalServerException;
    Relationship delete(Relationship relationship) throws InternalServerException;
    Relationship findByIdFromAndIdTo(Long userFromId, Long userToId) throws InternalServerException;
    List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException;
    List<Relationship> findByUserToId(Long userToId) throws InternalServerException;
    Relationship findByIds(Long userFromId, Long userToId) throws InternalServerException;
    List<Relationship> findByUserIdAndStatesRelationship(Long userId, FriendRelationshipStatus status) throws InternalServerException;
    Integer relationshipQuantityByUserId(Long userId, FriendRelationshipStatus status);
}
