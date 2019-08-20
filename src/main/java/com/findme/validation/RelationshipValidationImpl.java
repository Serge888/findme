package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.User;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RelationshipValidationImpl implements RelationshipValidation {
    @Value("${maxRequests:10}")
    private Integer maxRequests;
    @Value("${maxFriends:100}")
    private Integer maxFriends;


    @Override
    public Relationship relationshipValidation(User userFrom, User userTo, @NotNull Relationship relationship,
                                               FriendRelationshipStatus newStatus, Integer allRequests,
                                               Integer allFriendsUserFrom, Integer allFriendsUserTo) {

        // CURRENT STATUS - REQUESTED
        if (FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            return changeFromRequestValidation(relationship, newStatus, userFrom, userTo,
                    allFriendsUserFrom, allFriendsUserTo);
        }

        // CURRENT STATUS - ACCEPTED
        if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())
            && FriendRelationshipStatus.DELETED.equals(newStatus)) {
            return deleteValidation(relationship, newStatus);
        }

        // CURRENT STATUS - DELETED, CANCELED, DENIED, NULL
        if (FriendRelationshipStatus.DELETED.equals(relationship.getFriendRelationshipStatus())
            || FriendRelationshipStatus.CANCELED.equals(relationship.getFriendRelationshipStatus())
            || FriendRelationshipStatus.DENIED.equals(relationship.getFriendRelationshipStatus())
            || relationship.getFriendRelationshipStatus() == null) {
            return changeToRequestValidation(relationship, newStatus, userFrom, userTo, allRequests);
        }
        throw new BadRequestException("Invalid request.");
    }




    // - максимальный допустимый список друзей для одного пользователя - 100
    private Relationship changeFromRequestValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                          User userFrom, User userTo, Integer allFriendsUserFrom, Integer allFriendsUserTo) {
        if (relationship.getUserFrom().equals(userFrom)
                && FriendRelationshipStatus.CANCELED.equals(newStatus)) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        } else if (relationship.getUserFrom().equals(userTo)
                && FriendRelationshipStatus.ACCEPTED.equals(newStatus)) {
           friendQuantityValidation(allFriendsUserFrom, allFriendsUserTo);
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        } else if (relationship.getUserFrom().equals(userTo)
                && FriendRelationshipStatus.DENIED.equals(newStatus)) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("Invalid request");
    }


    // удалять с друзей можно если пользователи являются друзьями как минимум 3 дня
    private Relationship deleteValidation(Relationship relationship, FriendRelationshipStatus newStatus) {
        if (!FriendRelationshipStatus.DELETED.equals(newStatus)) {
            throw new BadRequestException("You can delete accepted status only");
        }
        if (LocalDate.now().minusDays(3).isBefore(relationship.getDateLastUpdated())) {
            throw new BadRequestException("You can remove a friend after at least three days of friendship.");
        }
        relationship.setFriendRelationshipStatus(newStatus);
        return relationship;
    }

    // - максимальное ко-во исходящих заявок в друзья для одного пользователя - 10
    private Relationship changeToRequestValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                                   User userFrom, User userTo, Integer allRequests) {
        if (FriendRelationshipStatus.REQUESTED.equals(newStatus)) {
            relationship.setFriendRelationshipStatus(newStatus);
            relationship.setUserFrom(userFrom);
            relationship.setUserTo(userTo);
            requestQuantityValidation(allRequests);
            return relationship;
        }
        throw new BadRequestException("Invalid request");
    }



    private void friendQuantityValidation(Integer allFriendsUserFrom, Integer allFriendsUserTo) {
        if (allFriendsUserFrom > maxFriends) {
            throw new BadRequestException("You can not have more than 100 friends.");
        }
        if (allFriendsUserTo > maxFriends) {
            throw new BadRequestException("Your recipient can not have more than 100 friends.");
        }
    }

    private void requestQuantityValidation(Integer allRequests) {
        if (allRequests > maxRequests) {
            throw new BadRequestException("You can not send more than 10 friend requests.");
        }
    }

}
