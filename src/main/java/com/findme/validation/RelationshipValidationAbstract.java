package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import lombok.Data;

@Data
abstract class RelationshipValidationAbstract {
    private RelationshipValidationAbstract next;

    Relationship nextRelationshipValidation(Relationship relationship, TechRelationshipData techRelationshipData) {
        Relationship returnRelationship = validation(relationship, techRelationshipData);
        if (next != null) {
            returnRelationship = next.validation(relationship, techRelationshipData);
        }
        return returnRelationship;
    }

    abstract Relationship validation(Relationship relationship,
                                     TechRelationshipData techRelationshipData) throws BadRequestException;


}
