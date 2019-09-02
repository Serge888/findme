package com.findme.validation.postValidation;

import com.findme.models.Post;
import lombok.NonNull;

public interface PostValidation {
    void postValidation(@NonNull Post post);

}
