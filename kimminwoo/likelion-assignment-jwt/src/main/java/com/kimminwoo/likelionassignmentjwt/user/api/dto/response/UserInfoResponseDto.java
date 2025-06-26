package com.kimminwoo.likelionassignmentjwt.user.api.dto.response;

import com.kimminwoo.likelionassignmentjwt.user.domain.User;
import lombok.Builder;

@Builder
public record UserInfoResponseDto(
        String email,
        String name,
        String token
) {
    public static UserInfoResponseDto of(User user, String token) {
        return UserInfoResponseDto.builder()
                .email(user.getEmail())
                .name(user.getUsername())
                .token(token)
                .build();
    }
}
