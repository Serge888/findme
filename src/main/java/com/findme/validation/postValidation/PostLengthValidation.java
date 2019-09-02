package com.findme.validation.postValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.Post;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
class PostLengthValidation extends PostValidationAbstract {


    @Override
    void validation(@NonNull Post post) throws BadRequestException {
        if (post.getMessage() != null && post.getMessage().length() <= 200) {
            return;
        }
        throw new BadRequestException("Post can't be null or more than 200 characters");
    }
}
