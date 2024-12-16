package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.auth.oauth.client.GithubOAuthLoginClient;
import gachagacha.gachagacha.auth.oauth.client.KakaoOAuthLoginClient;
import gachagacha.gachagacha.auth.oauth.dto.LoginResponse;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final GithubOAuthLoginClient githubOAuthLoginClient;
    private final KakaoOAuthLoginClient kakaoOAuthLoginClient;

    public LoginResponse loginWithGithub(String code) {
        String accessToken = githubOAuthLoginClient.fetchOAuthToken(code);
        Long userId = githubOAuthLoginClient.fetchOAuthUserId(accessToken);
        return generateLoginResponse(LoginType.GITHUB, userId);
    }

    public LoginResponse loginWithKakako(String code) {
        String accessToken = kakaoOAuthLoginClient.fetchOAuthToken(code);
        Long userId = kakaoOAuthLoginClient.fetchOAuthUserId(accessToken);
        return generateLoginResponse(LoginType.KAKAO, userId);
    }

    private LoginResponse generateLoginResponse(LoginType loginType, long userId) {
        Optional<User> optionalUser = userRepository.findByLoginTypeAndLoginId(loginType, userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname());
            return new LoginResponse(false, jwtDto, null, null);
        } else {
            return new LoginResponse(true, null, loginType.getName(), userId);
        }
    }
}
