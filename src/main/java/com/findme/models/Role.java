package com.findme.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_status")
    private RoleStatus roleStatus;
}
