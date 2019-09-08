package com.findme.relationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcceptedValidation extends RelationshipValidationAbstract {
    private Integer maxFriends = 100;
    private RelationshipService relationshipService;

    @Autowired
    public AcceptedValidation(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {

        if (!FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())
                || !relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                || !FriendRelationshipStatus.ACCEPTED.equals(techRelationshipData.getNewStatus())) {
            return relationship;
        }

        friendQuantityValidation(techRelationshipData);
        relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
        relationship.setValidated(true);

        return relationship;
    }


    private void friendQuantityValidation(TechRelationshipData techRelationshipData) {
        Integer allFriendsUserFrom = relationshipService.relationshipQuantityByUserId(techRelationshipData.getUserFrom().getId(),
                FriendRelationshipStatus.ACCEPTED);
        Integer allFriendsUserTo = relationshipService.relationshipQuantityByUserId(techRelationshipData.getUserTo().getId(),
                FriendRelationshipStatus.ACCEPTED);

        if (allFriendsUserFrom > maxFriends) {
            throw new BadRequestException("You can not have more than 100 friends.");
        }

        if (allFriendsUserTo > maxFriends) {
            throw new BadRequestException("Your recipient can not have more than 100 friends.");
        }
    }
}
