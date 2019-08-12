package com.findme.validation;

import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;

public interface Validation {
    Relationship relationshipValidation(Long userFromId, Long userToId,
                                        Relationship relationship, FriendRelationshipStatus newStatus);
}