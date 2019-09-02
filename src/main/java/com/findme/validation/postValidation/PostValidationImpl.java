package com.findme.validation.postValidation;

import com.findme.models.Post;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostValidationImpl implements PostValidation {
    private RelationshipService relationshipService;

    @Autowired
    public PostValidationImpl(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }


    @Override
    public void postValidation(Post post) {
        PostValidationAbstract postPageValidation = new PostPageValidation(relationshipService);
        PostValidationAbstract postLengthValidation = new PostLengthValidation();

        postPageValidation.setNext(postLengthValidation);

        postPageValidation.nextPostValidation(post);
    }
}
