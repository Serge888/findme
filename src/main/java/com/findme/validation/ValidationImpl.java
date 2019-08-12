package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidationImpl implements Validation {
    @Value("${maxRequests:10}")
    private Integer maxRequests;
    @Value("${maxFriends:100}")
    private Integer maxFriends;


    @Override
    public Relationship relationshipValidation(Long userFromId, Long userToId, @NotNull Relationship relationship,
                                               FriendRelationshipStatus newStatus) {

        // REQUESTED
        if (FriendRelationshipStatus.REQUESTED.equals(newStatus)) {
            return requestValidation(relationship, newStatus, userFromId, userToId);
        }

        // ACCEPTED
        if (FriendRelationshipStatus.ACCEPTED.equals(newStatus)) {
            return acceptValidation(relationship, newStatus, userFromId);
        }

        // DELETED
        if (FriendRelationshipStatus.DELETED.equals(newStatus)) {
            return deleteValidation(relationship, newStatus);
        }

        // CANCELED
        if (FriendRelationshipStatus.CANCELED.equals(newStatus)) {
            return canceledValidation(relationship, newStatus, userFromId);
        }

        // DENIED
        if (FriendRelationshipStatus.DENIED.equals(newStatus)) {
            return deniedValidation(relationship, newStatus, userFromId, userToId);
        }
        throw new BadRequestException("Unknown relationship status.");
    }



    // - максимальное ко-во исходящих заявок в друзья для одного пользователя - 10
    private Relationship requestValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                   Long userFromId, Long userToId) {

        if (relationship.getUserFromId().equals(userFromId)) {
            if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
                throw new BadRequestException("You are already friends.");
            }
            if (FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
                throw new BadRequestException("Request already was sent.");
            }
            requestQuantityValidation(relationship);
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        } else if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
            throw new BadRequestException("You are already friends.");
        } else if (FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            throw new BadRequestException("You've already received request from user id " + userToId
                    + " and now you can accept or deny it.");
        } else {
            requestQuantityValidation(relationship);
            relationship.setUserFromId(userFromId);
            relationship.setUserToId(userToId);
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
    }


    // - максимальный допустимый список друзей для одного пользователя - 100
    private Relationship acceptValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                  Long userFromId) {
        if (relationship.getUserFromId().equals(userFromId)
                && FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
           friendQuantityValidation(relationship);
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("You can accept requested status only");
    }




    // удалять с друзей можно если пользователи являются друзьями как минимум 3 дня
    private Relationship deleteValidation(Relationship relationship, FriendRelationshipStatus newStatus) {
        if (!FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
            throw new BadRequestException("You can delete accepted status only");
        }
        if (LocalDate.now().minusDays(3).isBefore(relationship.getDateLastUpdated())) {
            throw new BadRequestException("You can remove a friend after at least three days of friendship.");
        }
        relationship.setFriendRelationshipStatus(newStatus);
        return relationship;
    }


    private Relationship canceledValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                    Long userFromId) {
        if (relationship.getUserFromId().equals(userFromId) &&
                FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("You can cancel your requested status only");
    }


    private Relationship deniedValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                  Long userFromId, Long userToId) {
        if (relationship.getUserFromId().equals(userToId)
                && FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("You can denied other user requested status only");
    }


    private void friendQuantityValidation(Relationship relationship) {
        if (relationship.getFriendsQuantityUserFrom() > maxFriends) {
            throw new BadRequestException("You can not have more than 100 friends.");
        }
        if (relationship.getFriendsQuantityUserTo() > maxFriends) {
            throw new BadRequestException("Your recipient can not have more than 100 friends.");
        }
    }

    private void requestQuantityValidation(Relationship relationship) {
        if (relationship.getRequestQuantity() > maxRequests) {
            throw new BadRequestException("You can not send more than 10 friend requests.");
        }
    }

}
