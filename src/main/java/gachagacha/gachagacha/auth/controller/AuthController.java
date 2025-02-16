package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.service.AuthService;
import gachagacha.gachagacha.user.dto.JoinRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.authWithGithub(code);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.authWithKakao(code);
        response.sendRedirect(redirectUrl);
    }

    @PostMapping(value = "/join")
    public void join(@RequestPart(value = "data") JoinRequest joinRequest, @RequestPart(value = "profileImageFile") MultipartFile file, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.join(joinRequest, file);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping(value = "/image/profile", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getProfileImage() {
        ClassPathResource resource = new ClassPathResource("/profile/profile.png");
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping("/tokens/renew")
    public JwtDto renewTokens(HttpServletRequest request) {
        return authService.renewTokens(request);
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }
}
