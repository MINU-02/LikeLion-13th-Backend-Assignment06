package com.kimminwoo.likelionassignmentjwt.task.api;

import com.kimminwoo.likelionassignmentjwt.common.error.SuccessCode;
import com.kimminwoo.likelionassignmentjwt.common.template.ApiResTemplate;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.request.TaskSaveRequestDto;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.request.TaskUpdateRequestDto;
import com.kimminwoo.likelionassignmentjwt.task.api.dto.response.TaskListResponseDto;
import com.kimminwoo.likelionassignmentjwt.task.application.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class TaskController {

    private final TaskService taskService;

    // Task 생성
    @PostMapping("/tasks")
    public ApiResTemplate<String> createTask(@RequestBody @Valid TaskSaveRequestDto requestDto, Principal principal) {
        taskService.taskSave(requestDto, principal);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_SAVE_SUCCESS);
    }

    // 전체 Task 조회
    @GetMapping("/tasks")
    public ApiResTemplate<TaskListResponseDto> getAllTasks() {
        TaskListResponseDto responseDto = taskService.taskFindAll();
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, responseDto);
    }

    // 로그인한 사용자의 Task 조회
    @GetMapping("/users/me/tasks")
    public ApiResTemplate<TaskListResponseDto> getMyTasks(Principal principal) {
        TaskListResponseDto responseDto = taskService.taskFindByUser(principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, responseDto);
    }

    // Task 수정
    @PatchMapping("/tasks/{taskId}")
    public ApiResTemplate<String> updateTask(@PathVariable Long taskId,
                                             @RequestBody @Valid TaskUpdateRequestDto requestDto) {
        taskService.taskUpdate(taskId, requestDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_UPDATE_SUCCESS);
    }

    // Task 삭제
    @DeleteMapping("/tasks/{taskId}")
    public ApiResTemplate<String> deleteTask(@PathVariable Long taskId) {
        taskService.taskDelete(taskId);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_DELETE_SUCCESS);
    }
}
