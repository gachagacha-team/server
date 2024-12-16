package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.service.OAuth2LoginService;
import gachagacha.gachagacha.auth.oauth.dto.LoginResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2LoginController {

    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping("/login/oauth2/code/github")
    public LoginResponse login(@RequestParam(name = "code") String code) {
        return oAuth2LoginService.loginWithGithub(code);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public LoginResponse login2(@RequestParam(name = "code") String code) {
        return oAuth2LoginService.loginWithKakako(code);
    }
}
