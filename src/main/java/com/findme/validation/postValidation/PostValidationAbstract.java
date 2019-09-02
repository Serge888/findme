package com.findme.validation.postValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.Post;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
abstract class PostValidationAbstract {
    private PostValidationAbstract next;

    void nextPostValidation(Post post) {
        validation(post);
        if (next != null) {
            next.nextPostValidation(post);
        }
    }

    abstract void validation(Post post) throws BadRequestException;


    }
