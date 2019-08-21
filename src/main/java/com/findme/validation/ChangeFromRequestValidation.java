package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import org.springframework.beans.factory.annotation.Value;

class ChangeFromRequestValidation extends RelationshipValidationAbstract {
    @Value("${maxFriends:100}")
    private Integer maxFriends;

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        if (relationship.getUserFrom().equals(techRelationshipData.getUserFrom())
                && FriendRelationshipStatus.CANCELED.equals(techRelationshipData.getNewStatus())) {
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            return relationship;
        } else if (relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                && FriendRelationshipStatus.ACCEPTED.equals(techRelationshipData.getNewStatus())) {
            friendQuantityValidation(techRelationshipData.getAllFriendsUserFrom(),
                    techRelationshipData.getAllFriendsUserTo());
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            return relationship;
        } else if (relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                && FriendRelationshipStatus.DENIED.equals(techRelationshipData.getNewStatus())) {
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
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

}
