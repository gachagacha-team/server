package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
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
        Long userId = githubOAuthClient.fetchOAuthUserId(accessToken);
        return generateLoginResponse(LoginType.GITHUB, userId);
    }

    public AuthResponse authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        Long userId = kakaoOAuthClient.fetchOAuthUserId(accessToken);
        return generateLoginResponse(LoginType.KAKAO, userId);
    }

    private AuthResponse generateLoginResponse(LoginType loginType, long userId) {
        Optional<User> optionalUser = userRepository.findByLoginTypeAndLoginId(loginType, userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname());
            return new AuthResponse(false, jwtDto, null, null);
        } else {
            return new AuthResponse(true, null, loginType.getName(), userId);
        }
    }
}
