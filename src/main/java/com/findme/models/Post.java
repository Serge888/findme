package com.findme.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    //    само сообщение, максимальная допустимая длина 200 символов, так же в тексте запрещаются ссылки
    @Column(name = "message")
    private String message;

    @Column(name = "date_post")
    private LocalDate datePosted;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_posted_id", nullable = false)
    private User userPosted;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_page_posted_id", nullable = false)
    private User userPagePosted;

    @Column(name = "location")
    private String location; // - место прикрепленное к посту (никак не валидируется)

    @ManyToMany(
            targetEntity = User.class,
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_tagged",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> usersTagged; // - юзеры, которые отмечаны в после (аналок фейсбук функции - with user1, user2, user3)


    // TODO permission levels
    // TODO comments
}


