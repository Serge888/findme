package com.findme.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    // TODO from existing data
    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "age")
    private Integer age;

    @Column(name = "date_registered")
    private Date dateRegistered;

    @Column(name = "date_last_active")
    private Date dateLastActive;

    @Column(name = "religion")
    private String religion;

    // TODO from existing data
    @Column(name = "school")
    private String school;

    @Column(name = "university")
    private String university;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus relationShipStatus;


//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userFrom", cascade = CascadeType.ALL)
//    private List<Message> messageSent;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userTo", cascade = CascadeType.ALL)
//    private List<Message> messageReceived;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userPosted", cascade = CascadeType.ALL)
//    private List<Post> posts;

}
