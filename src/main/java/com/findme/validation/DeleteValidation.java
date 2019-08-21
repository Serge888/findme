package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;

import java.time.LocalDate;

class DeleteValidation extends RelationshipValidationAbstract {

    @Override
    Relationship validation(Relationship relationship, TechRelationshipData techRelationshipData) throws BadRequestException {
        if (!FriendRelationshipStatus.DELETED.equals(techRelationshipData.getNewStatus())) {
            throw new BadRequestException("You can delete accepted status only");
        }
        if (LocalDate.now().minusDays(3).isBefore(relationship.getDateLastUpdated())) {
            throw new BadRequestException("You can remove a friend after at least three days of friendship.");
        }
        relationship.setFriendRelationshipStatus(techRelationshipData.getNewStatus());
        return relationship;
    }
}
