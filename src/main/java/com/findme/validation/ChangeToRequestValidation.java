package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import org.springframework.beans.factory.annotation.Value;

public class ChangeToRequestValidation extends RelationshipValidationAbstract {
    @Value("${maxRequests:10}")
    private Integer maxRequests;

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        if (FriendRelationshipStatus.REQUESTED.equals(techRelationshipData.getNewStatus())) {
            relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
            relationship.setUserFrom(techRelationshipData.getUserFrom());
            relationship.setUserTo(techRelationshipData.getUserTo());
            requestQuantityValidation(techRelationshipData.getAllRequests());
            return relationship;
        }
        throw new BadRequestException("Invalid request");
    }

    private void requestQuantityValidation(Integer allRequests) {
        if (allRequests > maxRequests) {
            throw new BadRequestException("You can not send more than 10 friend requests.");
        }
    }
}
