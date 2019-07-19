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
@Table(name = "message")
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "date_sent")
    private Date dateSent;

    @Column(name = "date_read")
    private Date dateRead;


    @ManyToOne (optional = false, fetch=FetchType.LAZY)
    @JoinColumn (name = "user_from_id", nullable = false)
    private User userFrom;

    @ManyToOne (optional = false, fetch=FetchType.LAZY)
    @JoinColumn (name = "user_to_id", nullable = false)
    private User userTo;

}
