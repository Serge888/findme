package com.findme.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "password")
    private String password;


}
