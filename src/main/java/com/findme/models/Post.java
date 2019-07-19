package com.findme.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.xml.bind.annotation.XmlTransient;
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
