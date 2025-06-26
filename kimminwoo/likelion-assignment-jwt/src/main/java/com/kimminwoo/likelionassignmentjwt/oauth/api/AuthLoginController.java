package com.kimminwoo.likelionassignmentjwt.oauth.api;

import com.kimminwoo.likelionassignmentjwt.oauth.api.dto.Token;
import com.kimminwoo.likelionassignmentjwt.oauth.application.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;

//    // code : 인증 서버에서 받은 authorization code
//    // registrationId : 사용자가 로그인한 소셜 로그인의 id
//    @GetMapping("/code/{registrationID}")
//    public void googleLogin(@RequestParam String code, @PathVariable String registrationID){
//        authLoginService.socialLogin(code, registrationID);
//    }

    @GetMapping("/code/google")
    public Token googleCallback(@RequestParam(name = "code") String code){
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    public Token loginOrSignup(String googleAccessToken){
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}