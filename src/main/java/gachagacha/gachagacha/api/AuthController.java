package gachagacha.gachagacha.api;

import gachagacha.gachagacha.domain.SocialType;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.service.AuthService;
import gachagacha.gachagacha.api.dto.JoinRequest;
import gachagacha.gachagacha.service.OAuthService;
import gachagacha.gachagacha.service.UserService;
import gachagacha.gachagacha.support.api_response.ApiResponse;
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
    private final OAuthService oAuthService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Operation(summary = "깃허브 로그인 리다이렉트 URL", description = "깃허브 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.authWithGithub(code);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "카카오 로그인 리다이렉트 URL", description = "카카오 로그인 시 다음 URL로 리다이렉트된다.")
    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.authWithKakao(code);
        response.sendRedirect(redirectUrl);
    }

    @Operation(summary = "회원가입")
    @PostMapping(value = "/join")
    public void join(@RequestPart(value = "data") JoinRequest joinRequest, @RequestPart(value = "profileImageFile", required = false) MultipartFile file, HttpServletResponse response) throws IOException {
        User user = authService.join(joinRequest.getNickname(), SocialType.of(joinRequest.getSocialType()), joinRequest.getLoginId(), file);
        JwtDto jwtDto = jwtUtils.generateJwt(user);
        authService.saveRefreshToken(jwtDto.getRefreshToken());
        String redirectUrl = "https://gacha-holajjms-projects.vercel.app/" +
                "auth"
                + "?accessToken=" + jwtDto.getAccessToken()
                + "&refreshToken=" + jwtDto.getRefreshToken();
//        String redirectUrl = "http://localhost:5173/auth"
//                + "?accessToken=" + jwtDto.getAccessToken()
//                + "&refreshToken=" + jwtDto.getRefreshToken();
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
