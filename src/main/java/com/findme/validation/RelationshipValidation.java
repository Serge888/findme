package com.findme.validation;

import com.findme.models.Relationship;
import com.findme.models.TechRelationshipData;
import com.sun.istack.NotNull;

public interface RelationshipValidation {
    Relationship relationshipValidation(@NotNull Relationship relationship,
                                        @NotNull TechRelationshipData techRelationshipData);
}