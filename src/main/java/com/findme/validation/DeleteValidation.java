package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class DeleteValidation extends RelationshipValidationAbstract {

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        // CURRENT STATUS - ACCEPTED
        if (!FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())
                || !FriendRelationshipStatus.DELETED.equals(techRelationshipData.getNewStatus())) {
            return relationship;
        }
        if (LocalDate.now().minusDays(3).isBefore(relationship.getDateLastUpdated())) {
            throw new BadRequestException("You can remove a friend after at least three days of friendship.");
        }
        relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
        relationship.setValidated(true);
        return relationship;
    }
}
