package com.findme.models;

import lombok.*;

import javax.persistence.*;
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
