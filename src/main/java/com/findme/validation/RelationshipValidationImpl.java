package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.findme.service.RelationshipService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelationshipValidationImpl implements RelationshipValidation {
    private RelationshipService relationshipService;

    @Autowired
    public RelationshipValidationImpl(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @Override
    public Relationship relationshipValidation(@NotNull Relationship relationship,
                                               @NotNull TechRelationshipData techRelationshipData) {

        RelationshipValidationAbstract changeToRequestValidation = new ChangeToRequestValidation(relationshipService);
        RelationshipValidationAbstract changeFromRequestValidation = new ChangeFromRequestValidation(relationshipService);
        changeToRequestValidation.setNext(changeFromRequestValidation);
        RelationshipValidationAbstract deleteValidation = new DeleteValidation();
        changeFromRequestValidation.setNext(deleteValidation);

        relationship = changeToRequestValidation.nextRelationshipValidation(relationship, techRelationshipData);

        if (relationship.isValidated()) {
            return relationship;
        }
        throw new BadRequestException("Invalid request.");
    }
}
