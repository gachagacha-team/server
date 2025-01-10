package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuth2Service.authWithGithub(code);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuth2Service.authWithKakao(code);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping(value = "/image/profile", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getProfileImage() {
        ClassPathResource resource = new ClassPathResource("/profile/profile.png");
        return ResponseEntity.ok().body(resource);
    }
}
