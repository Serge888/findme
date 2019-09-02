package com.findme.validation.postValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Post;
import com.findme.models.Relationship;
import com.findme.service.RelationshipService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostPageValidation extends PostValidationAbstract {
    private RelationshipService relationshipService;

    @Autowired
    public PostPageValidation(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @Override
    void validation(@NonNull Post post) throws BadRequestException {
        if (post.getUserPagePosted() == null) {
            throw new BadRequestException("UserPagePosted can't be null.");
        }
        if (post.getUserPosted().equals(post.getUserPagePosted())) {
            return;
        }
        List<Relationship> relationshipList = relationshipService.findByUserIdAndStatesRelationship(post.getUserPosted().getId(),
                FriendRelationshipStatus.ACCEPTED);

        if (relationshipList != null && relationshipList.size() > 0) {
            for (Relationship relationship : relationshipList) {
                if (post.getUserPagePosted().equals(relationship.getUserFrom())
                        || post.getUserPagePosted().equals(relationship.getUserTo())) {
                    return;
                }
            }
        }

        throw new BadRequestException("You can create post on your or your friends' pages.");
    }
}
