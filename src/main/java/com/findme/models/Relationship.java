package com.findme.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@ToString
@EqualsAndHashCode
@Table(name = "relationship")
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "id_user_from")
    private Long userFromId;

    @Column(name = "id_user_to")
    private Long userToId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "id_relation_ship_status")
    private FriendRelationshipStatus friendRelationshipStatus;

    @Column (name = "date_created")
    private LocalDate dateCreated;

    @Column (name = "date_last_updated")
    private LocalDate dateLastUpdated;

    @Transient
    private Integer friendsQuantityUserFrom;
    @Transient
    private Integer friendsQuantityUserTo;
    @Transient
    private Integer requestQuantity;

}
