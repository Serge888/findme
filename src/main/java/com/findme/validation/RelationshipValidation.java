package com.findme.validation;

import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.sun.istack.NotNull;

public interface RelationshipValidation {
    Relationship relationshipValidation(User userFrom, User userTo, @NotNull Relationship relationship,
                                        FriendRelationshipStatus newStatus, Integer allRequests,
                                        Integer allFriendsUserFrom, Integer allFriendsUserTo);
}