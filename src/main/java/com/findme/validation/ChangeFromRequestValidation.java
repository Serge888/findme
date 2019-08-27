package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ChangeFromRequestValidation extends RelationshipValidationAbstract {
    private Integer maxFriends = 100;
    private RelationshipService relationshipService;

    @Autowired
    public ChangeFromRequestValidation(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        // CURRENT STATUS - REQUESTED
        if (!FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            return relationship;
        }
        int allFriendsUserFrom = relationshipService.relationshipQuantityByUserId(techRelationshipData.getUserFrom().getId(),
                FriendRelationshipStatus.ACCEPTED);
        int allFriendsUserTo = relationshipService.relationshipQuantityByUserId(techRelationshipData.getUserTo().getId(),
                FriendRelationshipStatus.ACCEPTED);
        if (relationship.getUserFrom().equals(techRelationshipData.getUserFrom())
                && FriendRelationshipStatus.CANCELED.equals(techRelationshipData.getNewStatus())) {
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            relationship.setValidated(true);
            return relationship;
        } else if (relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                && FriendRelationshipStatus.ACCEPTED.equals(techRelationshipData.getNewStatus())) {
            friendQuantityValidation(allFriendsUserFrom, allFriendsUserTo);
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            relationship.setValidated(true);
            return relationship;
        } else if (relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                && FriendRelationshipStatus.DENIED.equals(techRelationshipData.getNewStatus())) {
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            relationship.setValidated(true);
            return relationship;
        }
        return relationship;
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
