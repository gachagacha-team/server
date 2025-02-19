package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.service.AuthService;
import gachagacha.gachagacha.user.dto.JoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "깃허브 로그인 리다이렉트 URL", description = "깃허브 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.authWithGithub(code);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "카카오 로그인 리다이렉트 URL", description = "카카오 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.authWithKakao(code);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "회원가입")
    @PostMapping(value = "/join")
    public void join(@RequestPart(value = "data") JoinRequest joinRequest, @RequestPart(value = "profileImageFile", required = false) MultipartFile file, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.join(joinRequest, file);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "토큰 재발급", description = "액세스토큰 만료 시 액세스토큰과 리프레시토큰을 재발급받는다.")
    @PostMapping("/tokens/renew")
    public JwtDto renewTokens(HttpServletRequest request) {
        return authService.renewTokens(request);
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/withdraw")
    public void withdraw(HttpServletRequest request) {
        authService.withdraw(request);
    }
}
