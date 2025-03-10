package gachagacha.gachagacha.api;

import gachagacha.gachagacha.domain.user.Profile;
import gachagacha.gachagacha.domain.user.SocialType;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.domain.auth.AuthService;
import gachagacha.gachagacha.api.dto.request.JoinRequest;
import gachagacha.gachagacha.domain.auth.OAuthService;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Operation(summary = "깃허브 로그인 리다이렉트 URL", description = "깃허브 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        long loginId = oAuthService.authWithGithub(code);
        String redirectUrl = authService.generateRedirectUrl(loginId, SocialType.GITHUB);
        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "카카오 로그인 리다이렉트 URL", description = "카카오 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        long loginId = oAuthService.authWithKakao(code);
        String redirectUrl = authService.generateRedirectUrl(loginId, SocialType.KAKAO);
        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "회원가입")
    @PostMapping(value = "/join")
    public void join(@RequestBody JoinRequest requestDto, HttpServletResponse response) throws IOException {
        String redirectUrl = authService.join(requestDto.getNickname(), SocialType.of(requestDto.getSocialType()), requestDto.getLoginId(), Profile.findById(requestDto.getProfileId()));
        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "토큰 재발급", description = "access token 만료 시 access token과 refresh token을 재발급받는다. 헤더에 refresh token을 포함시켜야 한다.")
    @PostMapping("/tokens/renew")
    public ApiResponse renewTokens(HttpServletRequest request) {
        return ApiResponse.success(authService.renewTokens(request));
    }

    @Operation(summary = "로그아웃", description = "헤더에 refresh token을 포함시켜야 한다.")
    @DeleteMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponse.success();
    }

    @Operation(summary = "회원 탈퇴", description = "헤더에 refresh token을 포함시켜야 한다.")
    @DeleteMapping("/withdraw")
    public void withdraw(HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        authService.withdraw(user, jwtUtils.getRefreshTokenFromHeader(request));
        oAuthService.unlink(user);
    }
}
