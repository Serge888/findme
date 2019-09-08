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
public class TechPostData {
    private String message;
    private String location;
    private User userPosted;
    private String userPagePostedId;
    private String usersTagged;
}
