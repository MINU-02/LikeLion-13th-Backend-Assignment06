package com.kimminwoo.likelionassignmentjwt.user.api;

import com.kimminwoo.likelionassignmentjwt.common.error.SuccessCode;
import com.kimminwoo.likelionassignmentjwt.common.template.ApiResTemplate;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.request.UserJoinRequestDto;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.request.UserLoginRequestDto;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.response.UserInfoResponseDto;
import com.kimminwoo.likelionassignmentjwt.user.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ApiResTemplate<String> join(@RequestBody @Valid UserJoinRequestDto userJoinRequestDto) {
        userService.join(userJoinRequestDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.MEMBER_SAVE_SUCCESS);
    }

    @PostMapping("/login")
    public ApiResTemplate<UserInfoResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        UserInfoResponseDto userInfoResponseDto = userService.login(userLoginRequestDto);
        return ApiResTemplate.successResponse(SuccessCode.MEMBER_LOGIN_SUCCESS, userInfoResponseDto);
    }
}