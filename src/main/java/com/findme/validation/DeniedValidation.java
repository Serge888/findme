package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import org.springframework.stereotype.Component;

@Component
public class DeniedValidation extends RelationshipValidationAbstract {


    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {

        if (!FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())
                || !relationship.getUserFrom().equals(techRelationshipData.getUserTo())
                || !FriendRelationshipStatus.DENIED.equals(techRelationshipData.getNewStatus())) {
            return relationship;
        }

        relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
        relationship.setValidated(true);
        return relationship;
    }
}
