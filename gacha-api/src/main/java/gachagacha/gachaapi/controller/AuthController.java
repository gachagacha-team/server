package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.dto.response.UserInfoResponse;
import gachagacha.gachaapi.jwt.Jwt;
import gachagacha.gachaapi.service.AuthService;
import gachagacha.gachaapi.service.UserService;
import gachagacha.gachaapi.dto.request.UpdateUserInfoRequest;
import gachagacha.gachaapi.jwt.JwtUtils;
import gachagacha.domain.user.Profile;
import gachagacha.domain.user.SocialType;
import gachagacha.gachaapi.dto.request.JoinRequest;
import gachagacha.gachaapi.auth.OAuthService;
import gachagacha.gachaapi.response.ApiResponse;
import gachagacha.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${client.address}")
    private String clientAddress;

    @Operation(summary = "깃허브 로그인 리다이렉트 URL", description = "깃허브 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        long loginId = oAuthService.authWithGithub(code);
        String redirectUrl;
        Optional<User> optionalUser = authService.findUser(loginId, SocialType.GITHUB);
        if (optionalUser.isPresent()) {
            // 가입된 사용자 -> 토큰 발급(로그인)
            User user = optionalUser.get();
            Jwt jwt = jwtUtils.generateJwt(user.getId(), user.getProfile().getId());
            authService.saveRefreshToken(jwt.getRefreshToken());
            redirectUrl = clientAddress + "/auth"
                    + "?accessToken=" + jwt.getAccessToken()
                    + "&refreshToken=" + jwt.getRefreshToken();
        } else {
            // 새로운 사용자 -> 회원가입 폼으로
            redirectUrl = clientAddress + "/join"
                    + "?socialType=" + SocialType.GITHUB.getName()
                    + "&loginId=" + loginId;
        }

        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "카카오 로그인 리다이렉트 URL", description = "카카오 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        long loginId = oAuthService.authWithKakao(code);

        String redirectUrl;
        Optional<User> optionalUser = authService.findUser(loginId, SocialType.KAKAO);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Jwt jwt = jwtUtils.generateJwt(user.getId(), user.getProfile().getId());
            authService.saveRefreshToken(jwt.getRefreshToken());
            redirectUrl = clientAddress + "/auth"
                    + "?accessToken=" + jwt.getAccessToken()
                    + "&refreshToken=" + jwt.getRefreshToken();
        } else {
            redirectUrl = clientAddress + "/join"
                    + "?socialType=" + SocialType.GITHUB.getName()
                    + "&loginId=" + loginId;
        }

        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "회원가입")
    @PostMapping(value = "/join")
    public void join(@RequestBody JoinRequest requestDto, HttpServletResponse response) throws IOException {
        Jwt jwt = authService.join(requestDto.getNickname(), SocialType.of(requestDto.getSocialType()), requestDto.getLoginId(), Profile.findById(requestDto.getProfileId()));
        String redirectUrl = clientAddress + "/auth"
                + "?accessToken=" + jwt.getAccessToken()
                + "&refreshToken=" + jwt.getRefreshToken();
        log.info("Redirect. redirect url = {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "토큰 재발급", description = "access token 만료 시 access token과 refresh token을 재발급받는다. 헤더에 refresh token을 포함시켜야 한다.")
    @PostMapping("/tokens/renew")
    public ApiResponse<Jwt> renewTokens(HttpServletRequest request) {
        String refreshToken = jwtUtils.getRefreshTokenFromHeader(request);
        Long userId = jwtUtils.getUserIdFromHeader(request);
        return ApiResponse.success(authService.renewTokens(refreshToken, userId));
    }

    @Operation(summary = "로그아웃", description = "헤더에 refresh token을 포함시켜야 한다.")
    @DeleteMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        authService.logout(jwtUtils.getRefreshTokenFromHeader(request));
        return ApiResponse.success();
    }

    @Operation(summary = "회원 탈퇴", description = "헤더에 refresh token을 포함시켜야 한다.")
    @DeleteMapping("/withdraw")
    public void withdraw(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        authService.withdraw(user, jwtUtils.getRefreshTokenFromHeader(request));
        oAuthService.unlink(user);
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/user_info")
    public ApiResponse updateUserInfo(@RequestBody UpdateUserInfoRequest requestDto, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        userService.updateUserInfo(user, requestDto.getNickname(), requestDto.getProfileId());
        return ApiResponse.success();
    }

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/user_info")
    public ApiResponse<UserInfoResponse> readUserInfo(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        return ApiResponse.success(UserInfoResponse.of(user));
    }
}
