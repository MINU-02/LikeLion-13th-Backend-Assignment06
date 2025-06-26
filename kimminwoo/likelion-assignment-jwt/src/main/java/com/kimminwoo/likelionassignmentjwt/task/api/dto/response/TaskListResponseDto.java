package com.kimminwoo.likelionassignmentjwt.task.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TaskListResponseDto(
        List<TaskInfoResponseDto> tasks
) {
    public static TaskListResponseDto from(List<TaskInfoResponseDto> tasks) {
        return TaskListResponseDto.builder()
                .tasks(tasks)
                .build();
    }
}
