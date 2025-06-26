package com.kimminwoo.likelionassignmentjwt.user.domain;

import com.kimminwoo.likelionassignmentjwt.task.domain.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_email", nullable = false)
    private String email;
    private String password;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_picture_url")
    private String pictureUrl;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public User(String username, String email, String password, String pictureUrl, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.role = role;
    }

}
