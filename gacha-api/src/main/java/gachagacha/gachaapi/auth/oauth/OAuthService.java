package gachagacha.gachaapi.auth.oauth;

import gachagacha.gachaapi.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachaapi.auth.oauth.client.KakaoOAuthClient;
import gachagacha.domain.user.SocialType;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;

    public long authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        return githubOAuthClient.fetchOAuthLoginId(accessToken);
    }

    public long authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        return kakaoOAuthClient.fetchOAuthLoginId(accessToken);
    }

    public void unlink(User user) {
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoOAuthClient.unlink(user.getLoginId());
        }
    }
}
