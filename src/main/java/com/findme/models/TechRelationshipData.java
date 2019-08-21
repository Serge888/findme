package com.findme.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TechRelationshipData {
    private User userFrom;
    private User userTo;
    private Integer allRequests;
    private Integer allFriendsUserFrom;
    private Integer allFriendsUserTo;
    private FriendRelationshipStatus newStatus;
}
