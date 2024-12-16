package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.service.OAuth2Service;
import gachagacha.gachagacha.auth.oauth.dto.AuthResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/oauth2/code/github")
    public AuthResponse authWithGithub(@RequestParam(name = "code") String code) {
        return oAuth2Service.authWithGithub(code);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public AuthResponse authWithKakao(@RequestParam(name = "code") String code) {
        return oAuth2Service.authWithKakao(code);
    }
}
