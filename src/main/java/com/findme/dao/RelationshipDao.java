package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.Relationship;

import java.util.List;

public interface RelationshipDao {

    Relationship save(Relationship relationship) throws InternalServerException;
    Relationship findById(Long id) throws InternalServerException;
    Relationship update(Relationship relationship) throws InternalServerException;
    Relationship delete(Relationship relationship) throws InternalServerException;
    Relationship findByIdFromAndIdTo(Long userFromId, Long userToId) throws InternalServerException;
    List<Relationship> findByUserFromId(Long userFromId) throws InternalServerException;
    List<Relationship> findByUserToId(Long userToId) throws InternalServerException;

}