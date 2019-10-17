package com.findme.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "message")
@Data
@ToString
@EqualsAndHashCode (exclude = {"dateRead", "dateEdited", "dateDeleted"})
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", length = 140)
    private String text;

    @Column(name = "date_sent")
    private LocalDate dateSent;

    @Column(name = "date_read")
    private LocalDate dateRead;

    @Column(name = "date_edited")
    private LocalDate dateEdited;

    @Column(name = "date_deleted")
    private LocalDate dateDeleted;


    @ManyToOne (optional = false, fetch=FetchType.LAZY)
    @JoinColumn (name = "user_from_id", nullable = false)
    private User userFrom;

    @ManyToOne (optional = false, fetch=FetchType.LAZY)
    @JoinColumn (name = "user_to_id", nullable = false)
    private User userTo;

}
