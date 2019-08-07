package com.findme.validation;

public interface Validation {
    void deleteRelationshipValidation(Long userFromId, Long userToId);
    void friendQuantityValidation(Long userId);
    void requestQuantityValidation(Long userId);


}
