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

        // REQUESTED
        if (FriendRelationshipStatus.REQUESTED.equals(newStatus)) {
            return requestValidation(relationship, newStatus, userFrom, userTo, allRequests);
        }

        // ACCEPTED
        if (FriendRelationshipStatus.ACCEPTED.equals(newStatus)) {
            return acceptValidation(relationship, newStatus, userFrom,
                    allFriendsUserFrom, allFriendsUserTo);
        }

        // DELETED
        if (FriendRelationshipStatus.DELETED.equals(newStatus)) {
            return deleteValidation(relationship, newStatus);
        }

        // CANCELED
        if (FriendRelationshipStatus.CANCELED.equals(newStatus)) {
            return canceledValidation(relationship, newStatus, userFrom);
        }

        // DENIED
        if (FriendRelationshipStatus.DENIED.equals(newStatus)) {
            return deniedValidation(relationship, newStatus, userFrom);
        }
        throw new BadRequestException("Unknown relationship status.");
    }


    // - максимальное ко-во исходящих заявок в друзья для одного пользователя - 10
    private Relationship requestValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                           User userFrom, User userTo, Integer allRequests) {
        if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())
        || FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            throw new BadRequestException("Invalid request.");
        }
        requestQuantityValidation(allRequests);
        relationship.setUserFrom(userFrom);
        relationship.setUserTo(userTo);
        relationship.setFriendRelationshipStatus(newStatus);
        return relationship;
    }


    // - максимальный допустимый список друзей для одного пользователя - 100
    private Relationship acceptValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                          User userFrom, Integer allFriendsUserFrom, Integer allFriendsUserTo) {
        if (relationship.getUserFrom().equals(userFrom)
                && FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
           friendQuantityValidation(allFriendsUserFrom, allFriendsUserTo);
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
                                            User userFrom) {
        if (relationship.getUserFrom().equals(userFrom) &&
                FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("You can cancel your requested status only");
    }


    private Relationship deniedValidation(Relationship relationship, FriendRelationshipStatus newStatus,
                                          User userFrom) {
        if (relationship.getUserFrom().equals(userFrom)
                && FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            relationship.setFriendRelationshipStatus(newStatus);
            return relationship;
        }
        throw new BadRequestException("You can denied other user requested status only");
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
