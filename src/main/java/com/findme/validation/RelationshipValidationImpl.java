package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

@Component
public class RelationshipValidationImpl implements RelationshipValidation {

    @Override
    public Relationship relationshipValidation(@NotNull Relationship relationship,
                                               @NotNull TechRelationshipData techRelationshipData) {

        // CURRENT STATUS - REQUESTED
        if (FriendRelationshipStatus.REQUESTED.equals(relationship.getFriendRelationshipStatus())) {
            RelationshipValidationAbstract changeFromRequestValidation = new ChangeFromRequestValidation();
            return changeFromRequestValidation.nextRelationshipValidation(relationship, techRelationshipData);
        }

        // CURRENT STATUS - ACCEPTED
        if (FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())
            && FriendRelationshipStatus.DELETED.equals(techRelationshipData.getNewStatus())) {
            RelationshipValidationAbstract deleteValidation = new DeleteValidation();
            return deleteValidation.nextRelationshipValidation(relationship, techRelationshipData);

        }

        // CURRENT STATUS - DELETED, CANCELED, DENIED, NULL
        if (FriendRelationshipStatus.DELETED.equals(relationship.getFriendRelationshipStatus())
            || FriendRelationshipStatus.CANCELED.equals(relationship.getFriendRelationshipStatus())
            || FriendRelationshipStatus.DENIED.equals(relationship.getFriendRelationshipStatus())
            || relationship.getFriendRelationshipStatus() == null) {
            RelationshipValidationAbstract changeToRequestValidation = new ChangeFromRequestValidation();
            return changeToRequestValidation.nextRelationshipValidation(relationship, techRelationshipData);
        }
        throw new BadRequestException("Invalid request.");
    }
}
