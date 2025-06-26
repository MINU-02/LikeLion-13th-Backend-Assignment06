package com.kimminwoo.likelionassignmentjwt.global.jwt;


import com.kimminwoo.likelionassignmentjwt.common.error.ErrorCode;
import com.kimminwoo.likelionassignmentjwt.common.exception.BusinessException;
import com.kimminwoo.likelionassignmentjwt.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.kimminwoo.likelionassignmentjwt.user.domain.repository.UserRepository;

@Slf4j  // 로그 작성
@Component  // spring bean에 등록
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth"; // 권한 키 상수 추가

    @Value("${token.expire.time}") // factory annotation 임포트
    private String tokenExpireTime; // 토큰 만료 시간

    @Value("${jwt.secret}")
    private String secret;  // 비밀키

    private SecretKey key;  // 객체 key

    @PostConstruct  // Bean이 초기화 된 후에 실행
    public void init() {    // 필터 객체를 초기화하고 서비스에 추가하기 위한 메소드 init
        byte[] keyBytes = Decoders.BASE64.decode(secret);   // 시크릿 키 디코딩 후
        this.key = Keys.hmacShaKeyFor(keyBytes); // 키 암호화
    }

    // 토큰 생성
    public String generateToken(User user) {
        // 만료 시간 설정 util로 임포트
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + Long.parseLong(tokenExpireTime));

        return Jwts.builder()
                .subject(user.getUserId().toString())   // 토큰 주체를 id로 설정
                .claim(AUTHORITIES_KEY, user.getRole().toString()) // 추가된 권한 Claim
                .issuedAt(now)  // 발행 시간
                .expiration(expireDate) // 만료 시간
                .signWith(key, Jwts.SIG.HS256)  // ㅏ토큰 암호화
                .compact(); // 압축, 서명 후 토큰 생성
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);  // 토큰 파싱, 검증
            return true;    // 검증 완료 -> 유효한 토큰
            // 검증 실패 시 반환하는 예외에 따라 다르게 실행
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 유효하지 않습니다.");
        } catch (SignatureException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 서명 검증에 실패했습니다.");
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 null 이거나 비어있거나 공백만 있습니다.");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 검증에 실패했습니다.");
        }
    }

    // 인증 객체 반환 core 로 임포트
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String authority = claims.get(AUTHORITIES_KEY, String.class);
        if(authority == null) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "권한 정보가 없는 토큰입니다.");
        }

        List<GrantedAuthority> authorities = Arrays.stream(authority.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // memberId, 공백, authorities 반환
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // Claims 파싱
    private Claims parseClaims(String accessToken) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            return parser.parseSignedClaims(accessToken).getPayload();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}