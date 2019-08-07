package com.findme.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@ToString
@EqualsAndHashCode
@Table(name = "relationship")
@NamedQueries({
        @NamedQuery(
                name = "Relationship.findByUserFromId",
                query = "select r from Relationship r where r.userFromId = :userFromId"),
        @NamedQuery(
                name = "Relationship.findByUserToId",
                query = "select r from Relationship r where r.userToId = :userToId"),
        @NamedQuery(
                name = "Relationship.findByIdFromAndIdTo",
                query = "select r from Relationship r where r.userToId = :userToId and r.userFromId = :userFromId"),
        @NamedQuery(
                name = "Relationship.findByIds",
                query = "select r from Relationship r where (r.userToId = :userToId and r.userFromId = :userFromId) or "
                        + "(r.userToId = :userFromId and r.userFromId = :userToId)"),
        @NamedQuery(
                name = "Relationship.findByUserIdAndStatesRelationship",
                query = "select r from Relationship r where (r.userToId = :userId or r.userFromId = :userId) " +
                        "and r.friendRelationshipStatus = :friendRelationshipStatus")
})
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



}
