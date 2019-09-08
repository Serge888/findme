package com.findme.relationshipValidation;

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

        RelationshipValidationAbstract requestedValidation = new RequestedValidation(relationshipService);
        RelationshipValidationAbstract acceptedValidation = new AcceptedValidation(relationshipService);
        requestedValidation.setNext(acceptedValidation);

        RelationshipValidationAbstract deletedValidation = new DeletedValidation();
        acceptedValidation.setNext(deletedValidation);

        RelationshipValidationAbstract deniedValidation = new DeniedValidation();
        deletedValidation.setNext(deniedValidation);

        RelationshipValidationAbstract canceledValidation = new CanceledValidation();
        deniedValidation.setNext(canceledValidation);

        relationship = requestedValidation.nextRelationshipValidation(relationship, techRelationshipData);

        if (relationship.isValidated()) {
            return relationship;
        }
        throw new BadRequestException("Invalid request.");
    }
}
