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
public class PostFilter {
    private Long loggedInUser;
    private String userPageOwnerId;
    private String userPostId;
    private String friends;
    private Integer newsIndexFrom;
}
