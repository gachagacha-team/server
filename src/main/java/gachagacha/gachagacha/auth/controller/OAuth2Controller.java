package gachagacha.gachagacha.auth.controller;

import gachagacha.gachagacha.auth.service.OAuth2Service;
import gachagacha.gachagacha.auth.oauth.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private static final String REDIRECT_BASIC_URL = "http://localhost:5173/auth";

    @GetMapping("/login/oauth2/code/github")
    public void authWithGithub(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        AuthResponse authResponse = oAuth2Service.authWithGithub(code);
        String redirectUrl = REDIRECT_BASIC_URL + getParameter(authResponse);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void authWithKakao(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        AuthResponse authResponse = oAuth2Service.authWithKakao(code);

        String redirectUrl = REDIRECT_BASIC_URL + getParameter(authResponse);
        response.sendRedirect(redirectUrl);
    }

    private String getParameter(AuthResponse authResponse) {
        if (authResponse.isNewUser()) {
            String encodedNickname = URLEncoder.encode(authResponse.getNickname(), StandardCharsets.UTF_8);
            return "?isNewUser=" + authResponse.isNewUser()
                    + "&nickname=" + encodedNickname
                    + "&loginType=" + authResponse.getLoginType()
                    + "&loginId=" + authResponse.getLoginId()
                    + "&profileUrl=" + authResponse.getProfileUrl();
        } else {
            return "?isNewUser=" + authResponse.isNewUser()
                    + "&accessToken=" + authResponse.getJwtDto().getAccessToken()
                    + "&refreshToken=" + authResponse.getJwtDto().getRefreshToken();
        }
     }
}
