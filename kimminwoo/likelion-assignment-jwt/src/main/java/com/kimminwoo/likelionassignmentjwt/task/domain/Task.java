package com.kimminwoo.likelionassignmentjwt.task.domain;

import com.kimminwoo.likelionassignmentjwt.task.api.dto.request.TaskUpdateRequestDto;
import com.kimminwoo.likelionassignmentjwt.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public void update(TaskUpdateRequestDto taskUpdateRequestDto) {
        this.title = taskUpdateRequestDto.title();
        this.description = taskUpdateRequestDto.description();
    }
}
