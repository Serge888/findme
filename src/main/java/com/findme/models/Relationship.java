package com.findme.models;

import lombok.*;
import javax.persistence.*;

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
                query = "select r from Relationship r where r.userToId = :userToId and r.userFromId = :userFromId")
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


}
