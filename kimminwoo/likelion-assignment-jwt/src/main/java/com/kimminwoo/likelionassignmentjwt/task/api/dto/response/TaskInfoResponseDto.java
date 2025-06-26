package com.kimminwoo.likelionassignmentjwt.task.api.dto.response;

import com.kimminwoo.likelionassignmentjwt.task.domain.Task;
import lombok.Builder;

@Builder
public record TaskInfoResponseDto(
        String title,
        String description,
        String writer
) {
    public static TaskInfoResponseDto from(Task task) {
        return TaskInfoResponseDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .writer(task.getUser().getUsername())
                .build();
    }
}
