package com.findme.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "date_post")
    private Date datePosted;

    @ManyToOne (optional=false, fetch=FetchType.LAZY)
    @JoinColumn (name="user_posted_id", nullable=false)
    private User userPosted;

    // TODO permission levels
    // TODO comments
}
