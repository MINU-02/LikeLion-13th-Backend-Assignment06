package com.kimminwoo.likelionassignmentjwt.user.application;

import com.kimminwoo.likelionassignmentjwt.common.error.ErrorCode;
import com.kimminwoo.likelionassignmentjwt.common.exception.BusinessException;
import com.kimminwoo.likelionassignmentjwt.global.jwt.JwtTokenProvider;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.request.UserJoinRequestDto;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.request.UserLoginRequestDto;
import com.kimminwoo.likelionassignmentjwt.user.api.dto.response.UserInfoResponseDto;
import com.kimminwoo.likelionassignmentjwt.user.domain.Role;
import com.kimminwoo.likelionassignmentjwt.user.domain.User;
import com.kimminwoo.likelionassignmentjwt.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 (email, password, nickname)
    @Transactional
    public void join(UserJoinRequestDto userJoinRequestDto) {
        // 존재하는 이메일인지 확인
        if (userRepository.existsByEmail(userJoinRequestDto.email())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_EMAIL
                    , ErrorCode.ALREADY_EXISTS_EMAIL.getMessage());
        }

        // 패스워드 암호화하여 멤버 객체 생성
        User user = User.builder()
                .email(userJoinRequestDto.email())
                .password(passwordEncoder.encode(userJoinRequestDto.password()))
                .username(userJoinRequestDto.name())
                .role(Role.ROLE_USER)
                .build();

        // 멤버 저장
        userRepository.save(user);
    }

    // 로그인 (email, password)
    public UserInfoResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        // 멤버 찾기
        User user = userRepository.findByEmail(userLoginRequestDto.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION,
                        ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));

        // 비밀번호 일치 확인 로직u
        if (!passwordEncoder.matches(userLoginRequestDto.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        // 토큰 생성
        String token = jwtTokenProvider.generateToken(user);

        // 토큰과 함께 멤버 정보 리턴
        return UserInfoResponseDto.of(user, token);
    }

}