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

    @ManyToOne (optional=false, fetch=FetchType.EAGER)
    @JoinColumn (name="id_user_from", nullable=false)
    private User userFrom;

    @ManyToOne (optional=false, fetch=FetchType.EAGER)
    @JoinColumn (name="id_user_to", nullable=false)
    private User userTo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "id_relation_ship_status")
    private FriendRelationshipStatus friendRelationshipStatus;

    @Column (name = "date_created")
    private LocalDate dateCreated;

    @Column (name = "date_last_updated")
    private LocalDate dateLastUpdated;

}
