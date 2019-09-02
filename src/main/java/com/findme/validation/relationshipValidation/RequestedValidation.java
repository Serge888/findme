package com.findme.validation.relationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestedValidation extends RelationshipValidationAbstract {
    private Integer maxRequests = 10;
    private RelationshipService relationshipService;

    @Autowired
    public RequestedValidation(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }


    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        // CHECK CURRENT STATUS - DELETED, CANCELED, DENIED, NULL
        if ((FriendRelationshipStatus.DELETED.equals(relationship.getFriendRelationshipStatus())
                || FriendRelationshipStatus.CANCELED.equals(relationship.getFriendRelationshipStatus())
                || FriendRelationshipStatus.DENIED.equals(relationship.getFriendRelationshipStatus())
                || relationship.getFriendRelationshipStatus() == null)
                && FriendRelationshipStatus.REQUESTED.equals(techRelationshipData.getNewStatus())) {

            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            relationship.setUserFrom(techRelationshipData.getUserFrom());
            relationship.setUserTo(techRelationshipData.getUserTo());
            requestQuantityValidation(techRelationshipData);
            relationship.setValidated(true);
            return relationship;
        }
        return relationship;
    }

    private void requestQuantityValidation(TechRelationshipData techRelationshipData) {
        int allRequests = relationshipService.relationshipQuantityByUserId(techRelationshipData.getUserFrom().getId(),
                FriendRelationshipStatus.REQUESTED);

        if (allRequests > maxRequests) {
            throw new BadRequestException("You can not send more than 10 friend requests.");
        }
    }
}
