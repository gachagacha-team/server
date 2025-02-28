package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.SocialType;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.RefreshTokenEntity;
import gachagacha.gachagacha.implementation.user.UserReader;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.oauth.client.KakaoOAuthClient;
import gachagacha.gachagacha.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserReader userReader;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    public String authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        long loginId = githubOAuthClient.fetchOAuthLoginId(accessToken);
        return generateRedirectUrl(loginId, SocialType.GITHUB);
    }

    public String authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        long loginId = kakaoOAuthClient.fetchOAuthLoginId(accessToken);
        return generateRedirectUrl(loginId, SocialType.KAKAO);
    }

    private String generateRedirectUrl(long loginId, SocialType socialType) {
        Optional<User> optionalUser = userReader.findBySocialTypeAndLoginId(socialType, loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user);
            refreshTokenRepository.save(new RefreshTokenEntity(jwtDto.getRefreshToken()));
            return "https://gacha-ruddy.vercel.app/auth"
                    + "?accessToken=" + jwtDto.getAccessToken()
                    + "&refreshToken=" + jwtDto.getRefreshToken();
//            return "http://localhost:5173/auth"
//                    + "?accessToken=" + jwtDto.getAccessToken()
//                    + "&refreshToken=" + jwtDto.getRefreshToken();
        } else {
            return "https://gacha-ruddy.vercel.app/auth"
                    + "?socialType=" + socialType.getName()
                    + "&loginId=" + loginId;
//            return "http://localhost:5173/join"
//                    + "?socialType=" + socialType.getName()
//                    + "&loginId=" + loginId;
        }
    }

    public void unlink(User user) {
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoOAuthClient.unlink(user.getLoginId());
        }
    }
}
