package com.findme.validation;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;
import com.findme.models.Relationship;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ValidationImpl implements Validation {
    private RelationshipService relationshipService;
    @Value("${maxRequests:10}")
    private Integer maxRequests;
    @Value("${maxFriends:100}")
    private Integer maxFriends;

    @Autowired
    public ValidationImpl(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    // удалять с друзей можно если пользователи являются друзьями как минимум 3 дня
    public void deleteRelationshipValidation(Long userFromId, Long userToId) {
        Relationship relationship = relationshipService.findByIds(userFromId, userToId);
        if (relationship == null) {
            throw new BadRequestException("You don't have any relationship. userFromId: " + userFromId + " userToId " + userToId);
        }
        if (!FriendRelationshipStatus.ACCEPTED.equals(relationship.getFriendRelationshipStatus())) {
            throw new BadRequestException("You are not friends at this moment.");
        }
        if (LocalDate.now().minusDays(3).isBefore(relationship.getDateLastUpdated())) {
            throw new BadRequestException("You can remove a friend after at least three days of friendship.");
        }
    }


    // - максимальный допустимый список друзей для одного пользователя - 100
    @Override
    public void friendQuantityValidation(Long userId) {
        List<Relationship> relationship = relationshipService.findByUserIdAndStatesRelationship(userId, FriendRelationshipStatus.ACCEPTED);
        if (relationship != null && relationship.size() > maxFriends) {
            throw new BadRequestException("You can not have more than 100 friends.");
        }
    }


    // - максимальное ко-во исходящих заявок в друзья для одного пользователя - 10
    @Override
    public void requestQuantityValidation(Long userId) {
        List<Relationship> relationship = relationshipService.findByUserIdAndStatesRelationship(userId, FriendRelationshipStatus.REQUESTED);
        if (relationship != null && relationship.size() > maxRequests) {
            throw new BadRequestException("You can not send more than 10 friend requests.");
        }
    }


}
