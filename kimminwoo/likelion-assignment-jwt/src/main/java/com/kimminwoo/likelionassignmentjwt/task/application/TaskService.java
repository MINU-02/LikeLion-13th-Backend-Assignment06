package com.kimminwoo.likelionassignmentjwt.task.application;

import com.kimminwoo.likelionassignmentjwt.common.error.ErrorCode;
import com.kimminwoo.likelionassignmentjwt.common.exception.BusinessException;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.request.TaskSaveRequestDto;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.request.TaskUpdateRequestDto;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.response.TaskInfoResponseDto;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.response.TaskListResponseDto;
import com.kimminwoo.likelionassignmentjwt.task.domain.Task;
import com.kimminwoo.likelionassignmentjwt.task.domain.repository.TaskRepository;
import com.kimminwoo.likelionassignmentjwt.user.domain.User;
import com.kimminwoo.likelionassignmentjwt.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // Task 생성
    @Transactional
    public void taskSave(TaskSaveRequestDto requestDto, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION,
                        ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + userId));

        Task task = Task.builder()
                .title(requestDto.title())
                .description(requestDto.description())
                .user(user)
                .build();

        taskRepository.save(task);
    }

    // 전체 Task 조회
    public TaskListResponseDto taskFindAll() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskInfoResponseDto> response = tasks.stream()
                .map(TaskInfoResponseDto::from)
                .toList();

        return TaskListResponseDto.from(response);
    }

    // 로그인 사용자 Task 조회
    public TaskListResponseDto taskFindByUser(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION,
                        ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + userId));

        List<Task> tasks = taskRepository.findByUser(user);
        List<TaskInfoResponseDto> response = tasks.stream()
                .map(TaskInfoResponseDto::from)
                .toList();

        return TaskListResponseDto.from(response);
    }

    // Task 수정
    @Transactional
    public void taskUpdate(Long taskId, TaskUpdateRequestDto requestDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND_EXCEPTION,
                        ErrorCode.POST_NOT_FOUND_EXCEPTION.getMessage() + taskId)
        );
        task.update(requestDto);
    }

    // Task 삭제
    @Transactional
    public void taskDelete(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND_EXCEPTION,
                        ErrorCode.POST_NOT_FOUND_EXCEPTION.getMessage() + taskId)
        );
        taskRepository.delete(task);
    }
}
