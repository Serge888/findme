package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.service.RelationshipService;
import com.findme.util.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class UserValidationImpl implements UserValidation {
    private RelationshipService relationshipService;

    @Autowired
    public UserValidationImpl(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @Override
    public void viewProfileValidation(HttpSession session, Long profileUserId) throws BadRequestException {
        if (session.getAttribute("id") == null) {
            throw new BadRequestException("First you should login.");
        }
        Long loggedInUserId = UtilString.stringToLong(session.getAttribute("id").toString());
        Relationship relationship = relationshipService.findByIds(loggedInUserId, profileUserId);
        if ((relationship == null && !loggedInUserId.equals(profileUserId))
                || (!loggedInUserId.equals(profileUserId)
                        && !FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus()))) {
            throw new BadRequestException("You can view your profile or your friends only.");
        }
    }

    @Override
    public void isUserLoggedIn(HttpSession session, String userIdFrom) throws BadRequestException {
        if (session.getAttribute("id") != UtilString.stringToLong(userIdFrom)) {
            throw new BadRequestException("First you should login.");
        }
    }

}
