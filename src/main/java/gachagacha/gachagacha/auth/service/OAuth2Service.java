package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.auth.oauth.dto.UserInfo;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.auth.oauth.client.KakaoOAuthClient;
import gachagacha.gachagacha.auth.oauth.dto.AuthResponse;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;

    public AuthResponse authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        UserInfo userInfo = githubOAuthClient.fetchOAuthUserInfo(accessToken);
        return generateLoginResponse(LoginType.GITHUB, userInfo);
    }

    public AuthResponse authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        UserInfo userInfo = kakaoOAuthClient.fetchOAuthUserInfo(accessToken);
        return generateLoginResponse(LoginType.KAKAO, userInfo);
    }

    private AuthResponse generateLoginResponse(LoginType loginType, UserInfo userInfo) {
        Optional<User> optionalUser = userRepository.findByLoginTypeAndLoginId(loginType, userInfo.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user.getId());
            return new AuthResponse(false, jwtDto, null, null, null, null);
        } else {
            return new AuthResponse(true, null, loginType.getName(), userInfo.getId(), userInfo.getNickname(), userInfo.getProfileImageUrl());
        }
    }
}
